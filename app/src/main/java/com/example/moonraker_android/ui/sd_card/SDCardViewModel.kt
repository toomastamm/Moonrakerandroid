package com.example.moonraker_android.ui.sd_card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SDCardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is SD card Fragment"
    }
    val text: LiveData<String> = _text
}