package com.example.moonraker_android.ui.print_status

import android.util.Log
import com.github.kittinunf.fuel.core.extensions.cUrlString
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result

object PrintStatusAPI {

    private const val TAG = "PrintStatusAPI"

    fun getState(): String {
        val path = "/printer/info"

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
                    }
                }
            }

        return ""
    }
}