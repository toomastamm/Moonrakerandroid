package com.example.moonraker_android.api

import android.content.Context
import android.content.SharedPreferences
import com.github.kittinunf.fuel.core.FuelManager

object MoonrakerService {

    private lateinit var baseUrl: String
    private const val PREFERENCES_NAME = "moonraker_preferences"
    private lateinit var preferences: SharedPreferences

    private const val PREFS_MOONRAKER_HOST = "moonraker_host"
    private const val PREFS_MOONRAKER_HOST_DEFAULT = "127.0.0.1"

    private const val PREFS_MOONRAKER_PORT = "moonraker_port"
    private const val PREFS_MOONRAKER_PORT_DEFAULT = "8080"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val host = preferences.getString(PREFS_MOONRAKER_HOST, PREFS_MOONRAKER_HOST_DEFAULT)
        val port = preferences.getString(PREFS_MOONRAKER_PORT, PREFS_MOONRAKER_PORT_DEFAULT)
        baseUrl = host + port
        FuelManager.instance.basePath = baseUrl
    }
}