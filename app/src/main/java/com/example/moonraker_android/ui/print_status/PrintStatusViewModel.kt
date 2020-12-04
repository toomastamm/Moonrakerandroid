package com.example.moonraker_android.ui.print_status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrintStatusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is print status Fragment"
    }
    val text: LiveData<String> = _text
}