package com.example.moonraker_android.ui.sd_card

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId

object SDCardAPI {

    private const val TAG = "SDCardAPI"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFiles(_state: MutableLiveData<ArrayList<SDCardFileResponse>>) {
        val path = "/server/files/list?root=gcodes"

        path.httpGet()
            .also { Log.d(TAG, "Loading files from ${it.url}") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val resp = SDCardFileResponse(
                            error = ex.localizedMessage,
                            filename = "",
                            modified = null,
                            size = "0 MB",
                        )
                        val files = arrayListOf(resp)
                        _state.postValue(files)
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        val files = arrayListOf<SDCardFileResponse>()
                        for (i in 0 until data.getJSONArray("result").length()) {
                            val fileObject = JSONObject(data.getJSONArray("result").get(i).toString())
                            val response = SDCardFileResponse(
                                error = null,
                                filename = fileObject.getString("filename"),
                                modified = Instant.ofEpochSecond(fileObject.getDouble("modified").toLong())
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                size = (fileObject.getLong("size")/1000000.00).toString() + " MB",
                            )
                            files.add(response)
                        }
                        _state.postValue(files)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFileMetadata(_state: MutableLiveData<SDCardFileMetaDataResponse>, fileName: String) {
        val path = "/server/files/metadata?filename=$fileName"

        path.httpGet()
            .also { Log.d(TAG, "Loading file $fileName metadata from ${it.url}") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val response = SDCardFileMetaDataResponse(
                            error = ex.localizedMessage,
                            estimatedTime = "",
                            slicerName = "",
                            slicerVersion = "",
                        )
                        _state.postValue(response)
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        val metaDataObject = data.getJSONObject("result")
                        val response = SDCardFileMetaDataResponse(
                            error = null,
                            estimatedTime = secondsToHoursMinutesSeconds(metaDataObject.getLong("estimated_time")),
                            slicerName = metaDataObject.getString("slicer"),
                            slicerVersion = metaDataObject.getString("slicer_version"),
                        )
                        _state.postValue(response)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun printFile(fileName: String, context: Context) {
        val path = "/printer/print/start?filename=$fileName"

        path.httpPost()
            .also { Log.d(TAG, "Sending file $fileName to the printer ${it.url} for printing.") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        if (ex.message.equals("HTTP Exception 400 Bad Request")) {
                            Toast.makeText(context,"Printer is already printing!", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(TAG, "Response to the POST request: $data")
                    }
                }
            }
    }

    private fun secondsToHoursMinutesSeconds(time: Long) : String{
        val hours = time / 3600
        val minutes = (time - hours * 3600) / 60
        val seconds = time - hours * 3600 - minutes * 60
        return hours.toString() + "h " + minutes.toString() + "m " + seconds.toString() + "s"
    }
}