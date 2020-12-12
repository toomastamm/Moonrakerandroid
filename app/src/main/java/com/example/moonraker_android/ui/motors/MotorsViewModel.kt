package com.example.moonraker_android.ui.motors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moonraker_android.ui.print_status.PrintStatusAPI
import com.example.moonraker_android.ui.print_status.StatusResponse

class MotorsViewModel : ViewModel() {

    private val _state = MutableLiveData<MotorsResponse>()
    val state: MutableLiveData<MotorsResponse>
        get() = _state

    fun loadStatus() {
        MotorsAPI.getStatus(_state)
    }

}