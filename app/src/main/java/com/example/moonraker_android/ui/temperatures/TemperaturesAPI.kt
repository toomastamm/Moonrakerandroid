package com.example.moonraker_android.ui.temperatures

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result

object TemperaturesAPI {

    private const val TAG = "TemperaturesAPI"

    fun getStatus(
        _state: MutableLiveData<TemperaturesResponse>,
        heaterBeds: List<String>,
        extruders: List<String>
    ) {

        var path = "/printer/objects/query?"

        for (bed in heaterBeds) {
            path += "&$bed"
        }

        for (extruder in extruders) {
            path += "&$extruder"
        }


        path.httpGet()
            .also { Log.d(TAG, "Loading temperatures from ${it.url}") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val resp = TemperaturesResponse(
                            heater_beds = HashMap(),
                            extruders = HashMap()
                        )
                        _state.postValue(resp)
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(TAG, "Success response: $data")
                        val status = data.getJSONObject("result").getJSONObject("status")

                        val heaterBedsTemperatures = HashMap<String, ObjectTemperatures>()
                        val extrudersTemperatures = HashMap<String, ObjectTemperatures>()

                        for (bed in heaterBeds) {
                            val currentBed = status.getJSONObject(bed)
                            heaterBedsTemperatures[bed] = ObjectTemperatures(
                                current = currentBed.getDouble("temperature"),
                                target = currentBed.getDouble("target")
                            )
                        }

                        for (extruder in extruders) {
                            val currentExtruder = status.getJSONObject(extruder)
                            heaterBedsTemperatures[extruder] = ObjectTemperatures(
                                current = currentExtruder.getDouble("temperature"),
                                target = currentExtruder.getDouble("target")
                            )

                        }


                        val resp = TemperaturesResponse(
                            extruders = extrudersTemperatures,
                            heater_beds = heaterBedsTemperatures
                        )

                        Log.d(TAG, resp.toString())
                        _state.postValue(resp)
                    }
                }
            }
    }

}