package com.example.moonraker_android.utils

object Utils {

    fun secondsToHoursMinutesSeconds(time: Long) : String{
        val hours = time / 3600
        val minutes = (time - hours * 3600) / 60
        val seconds = time - hours * 3600 - minutes * 60
        return hours.toString() + "h " + minutes.toString() + "m " + seconds.toString() + "s"
    }
}