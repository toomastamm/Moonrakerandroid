package com.example.moonraker_android.ui.print_status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrintStatusViewModel : ViewModel() {

    private val state: MutableLiveData<String> = MutableLiveData<String>().also {
        loadState()
    }

    fun getState(): LiveData<String> {
        return state
    }

    private fun loadState() {
        // Do an asynchronous operation to fetch users.
    }
}