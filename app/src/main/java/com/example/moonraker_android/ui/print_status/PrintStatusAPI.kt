package com.example.moonraker_android.ui.print_status

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object PrintStatusAPI {

    private const val LOGGING_TAG = "PrintStatusAPI"
    private const val PREFERENCES_NAME = ""
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getStatus(): Job {
        val url = "${preferences.getString("moonraker_ip", "")}/printer/info"
        Log.d(LOGGING_TAG, "Loading status from $url")

        val httpAsync = url.httpGet()
            .responseJson { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e(LOGGING_TAG, "Error response: $ex")
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(LOGGING_TAG, "Success response: $data")
                    }
                }
            }

        return MainScope().launch(Dispatchers.IO) {
            httpAsync.join()
        }
    }
}