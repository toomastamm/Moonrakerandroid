package com.example.moonraker_android.ui.sd_card

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.moonraker_android.utils.Utils
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId

object SDCardAPI {

    private const val TAG = "SDCardAPI"

    fun getFiles(_state: MutableLiveData<ArrayList<SDCardFileResponse>>) {
        //Log.d("SDCardAPI", "SDCardAPI, getFiles")
        val path = "/server/files/list?root=gcodes"

        path.httpGet()
            .also { Log.d(TAG, "SDCardAPI, Loading files from ${it.url}") } // WORKS
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        Log.d("SDCardAPI", "SDCardAPI, getFile ERROR") // Not executed
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val resp = SDCardFileResponse(
                            error = ex.localizedMessage,
                            filename = "",
                            modified = "",
                            size = "0 MB",
                        )
                        val files = arrayListOf(resp)
                        _state.postValue(files)
                    }
                    is Result.Success -> {
                        //Log.d("SDCardAPI", "SDCardAPI, getFiles") // WORKS
                        val data = result.get().obj()
                        val files = arrayListOf<SDCardFileResponse>()
                        for (i in 0 until data.getJSONArray("result").length()) {
                            val fileObject = JSONObject(data.getJSONArray("result").get(i).toString())
                            val response = SDCardFileResponse(
                                error = null,
                                filename = fileObject.getString("filename"),
                                modified = Instant.ofEpochSecond(fileObject.getDouble("modified").toLong())
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime().toString(),
                                size = (fileObject.getLong("size")/1000000.00).toString() + " MB",
                            )
                            files.add(response)
                        }
                        //Log.d("SDCardAPI", "SDCardAPI, getFiles end") // WORKS
                        _state.postValue(files)
                    }
                }
            }
    }

    fun getFileMetadata(_state: MutableLiveData<SDCardFileMetaDataResponse>, fileName: String) {
        val path = "/server/files/metadata?filename=$fileName"

        path.httpGet()
            .also { Log.d(TAG, "SDCardAPI, Loading metadata for file $fileName from ${it.url}") } // WORKS, 2 TIMES
            .responseJson { _, _, result ->
                Log.d("SDCardAPI", "SDCardAPI, getFileMetadata result: $result")
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val response = SDCardFileMetaDataResponse(
                            fileName = "",
                            error = ex.localizedMessage,
                            estimatedTime = "",
                            slicerName = "",
                            slicerVersion = "",
                        )
                        Log.d("SDCardAPI", "SDCardAPI, metadata ERROR!!!!!") // NOT EXECUTED
                        _state.postValue(response)
                    }
                    is Result.Success -> {
                        //Log.d("SDCardAPI", "SDCardAPI, getFileMetadata") // 1 WORKS
                        val data = result.get().obj()
                        val metaDataObject = data.getJSONObject("result")
                        val response = SDCardFileMetaDataResponse(
                            fileName = metaDataObject.getString("filename"),
                            error = null,
                            estimatedTime = Utils.secondsToHoursMinutesSeconds(metaDataObject.getLong("estimated_time")),
                            slicerName = metaDataObject.getString("slicer"),
                            slicerVersion = metaDataObject.getString("slicer_version"),
                        )
                        //Log.d("SDCardAPI", "SDCardAPI, getFileMetadata end: $response") // 1 WORKS
                        _state.postValue(response)
                    }
                }
            }
    }

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
                        Log.d(TAG, "SDCardAPI, Response to the POST print request: $data")
                    }
                }
            }
    }


}