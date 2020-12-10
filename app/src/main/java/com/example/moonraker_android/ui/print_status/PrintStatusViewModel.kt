package com.example.moonraker_android.ui.print_status

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class PrintStatusViewModel : ViewModel() {

    val state: LiveData<String> = liveData {
        val data = PrintStatusAPI.getState()
        emit(data)
    }
}