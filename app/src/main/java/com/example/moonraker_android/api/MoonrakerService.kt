package com.example.moonraker_android.api

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.github.kittinunf.fuel.core.FuelManager

object MoonrakerService {

    lateinit var baseUrl: String
    private lateinit var preferences: SharedPreferences

    private const val PREFS_MOONRAKER_HOST = "moonraker_host"
    private const val PREFS_MOONRAKER_HOST_DEFAULT = "http://127.0.0.1"

    private const val PREFS_MOONRAKER_PORT = "moonraker_port"
    private const val PREFS_MOONRAKER_PORT_DEFAULT = "8080"

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val host = preferences.getString(PREFS_MOONRAKER_HOST, PREFS_MOONRAKER_HOST_DEFAULT)
        val port = preferences.getString(PREFS_MOONRAKER_PORT, PREFS_MOONRAKER_PORT_DEFAULT)
        baseUrl = "$host:$port"
        FuelManager.instance.basePath = baseUrl
    }
}