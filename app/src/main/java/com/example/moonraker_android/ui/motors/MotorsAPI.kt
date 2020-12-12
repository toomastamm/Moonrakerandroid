package com.example.moonraker_android.ui.motors

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result

object MotorsAPI {

    private const val TAG = "MotorsAPI"

    fun getStatus(
        _state: MutableLiveData<MotorsResponse>,
    ) {

        val path = "/printer/objects/query?toolhead"

        path.httpGet()
            .also { Log.d(TAG, "Loading temperatures from ${it.url}") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                        val resp = MotorsResponse(
                            X = 0.0,
                            Y = 0.0,
                            Z = 0.0,
                            E = 0.0
                        )
                        _state.postValue(resp)
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(TAG, "Success response: $data")
                        val positions = data.getJSONObject("result").getJSONObject("status")
                            .getJSONObject("toolhead").getJSONArray("position")

                        val resp = MotorsResponse(
                            X = positions.getDouble(0),
                            Y = positions.getDouble(1),
                            Z = positions.getDouble(2),
                            E = positions.getDouble(3)
                        )

                        Log.d(TAG, resp.toString())
                        _state.postValue(resp)
                    }
                }
            }
    }

}