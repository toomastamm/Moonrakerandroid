package com.example.moonraker_android.ui.print_status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrintStatusViewModel : ViewModel() {
    private val _state = MutableLiveData<StatusResponse>()
    val state: LiveData<StatusResponse>
        get() = _state

    fun loadStatus() {
        PrintStatusAPI.getStatus(_state)
    }
}