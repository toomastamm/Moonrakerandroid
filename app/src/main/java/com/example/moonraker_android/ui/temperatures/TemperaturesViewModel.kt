package com.example.moonraker_android.ui.temperatures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TemperaturesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is temperatures Fragment"
    }
    val text: LiveData<String> = _text
}