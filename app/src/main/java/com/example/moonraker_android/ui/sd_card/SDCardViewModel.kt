package com.example.moonraker_android.ui.sd_card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SDCardViewModel : ViewModel() {

    private val _state = MutableLiveData<ArrayList<SDCardFileResponse>>()
    val state: MutableLiveData<ArrayList<SDCardFileResponse>>
        get() = _state

    private val _metaData = MutableLiveData<SDCardFileMetaDataResponse>()
    val metaData: MutableLiveData<SDCardFileMetaDataResponse>
        get() = _metaData

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadFiles() {
        SDCardAPI.getFiles(_state)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadFileDetails(fileName: String) {
        SDCardAPI.getFileMetadata(_metaData, fileName)
    }
}