package com.example.moonraker_android.ui.print_status

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.core.extensions.cUrlString
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result

object PrintStatusAPI {

    private const val TAG = "PrintStatusAPI"

    fun getStatus(_state: MutableLiveData<StatusResponse>) {
        val path = "https://mockprint.toomastamm.ee/printer/objects/query?print_stats"

        path.httpGet()
            .also { Log.d(TAG, "Loading state from ${it.cUrlString()}") }
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(TAG, "Error response: $ex")
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(TAG, "Success response: $data")
                        val stats = data.getJSONObject("result").getJSONObject("status").getJSONObject("print_stats")
                        val resp = StatusResponse(
                            state = stats.getString("state"),
                            filename = stats.getString("filename"),
                            message = stats.getString("message"),
                            print_duration = stats.getDouble("print_duration"),
                            total_duration = stats.getDouble("total_duration"),
                        )
                        _state.postValue(resp)
                    }
                }
            }
    }
}