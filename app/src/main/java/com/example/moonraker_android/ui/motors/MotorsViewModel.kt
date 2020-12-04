package com.example.moonraker_android.ui.motors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MotorsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is motors Fragment"
    }
    val text: LiveData<String> = _text
}