package com.example.moonraker_android.ui.temperatures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.ui.print_status.PrintStatusAPI
import com.example.moonraker_android.ui.print_status.StatusResponse

class TemperaturesViewModel : ViewModel() {

    private val _state = MutableLiveData<TemperaturesResponse>()
    val state: MutableLiveData<TemperaturesResponse>
        get() = _state

    fun loadStatus(
        heaterBeds: List<String>,
        extruders: List<String>
    ) {
        TemperaturesAPI.getStatus(_state, heaterBeds, extruders)
    }
}