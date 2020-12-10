package com.example.moonraker_android.ui.print_status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrintStatusViewModel : ViewModel() {
    private val _state = MutableLiveData<String>()
    val state: LiveData<String>
        get() = _state

    val file: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun loadState() {
        _state.postValue(PrintStatusAPI.getState())
    }

    fun loadFile() {
        file.postValue("some-file.gcode")
    }
}