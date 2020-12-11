package com.example.moonraker_android.api

import android.content.Context
import android.content.SharedPreferences
import com.github.kittinunf.fuel.core.FuelManager

object MoonrakerService {

    private lateinit var baseUrl: String
    private const val PREFERENCES_NAME = ""
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        baseUrl = preferences.getString("moonraker_ip", "https://mockprint.toomastamm.ee").toString()
        FuelManager.instance.basePath = baseUrl
    }

    fun request(path: String) {

    }
}