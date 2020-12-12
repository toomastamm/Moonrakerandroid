package com.example.moonraker_android.ui.temperatures

data class TemperaturesResponse(
    val heater_beds: Map<String, ObjectTemperatures>,
    val extruders: Map<String, ObjectTemperatures>
)