package com.example.moonraker_android.ui.sd_card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moonraker_android.room.RoomDb

class SDCardViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<ArrayList<SDCardFileResponse>>()
    private val _metaData = MutableLiveData<SDCardFileMetaDataResponse>()
    private var roomDb: RoomDb = RoomDb.getInstance(application)

    val state: MutableLiveData<ArrayList<SDCardFileResponse>>
        get() = _state

    val metaData: MutableLiveData<SDCardFileMetaDataResponse>
        get() = _metaData

    fun loadFiles() {
        SDCardAPI.getFiles(_state)
    }

    fun loadFileDetails(fileName: String) {
        SDCardAPI.getFileMetadata(_metaData, fileName)
    }

    fun getFileByName(fileName: String): SDCardFileResponse {
        return roomDb.getFileDao().getFileByName(fileName)
    }

    fun getFileNames(): ArrayList<String> {
        return ArrayList(roomDb.getFileDao().getFileNames().toList())
    }

    fun addFile(file: SDCardFileResponse) {
        roomDb.getFileDao().insertFile(file)
    }

    fun addFileMetadata(metadata: SDCardFileMetaDataResponse) {
        roomDb.getFileDao().insertFileMetaData(metadata)
    }

    fun getFileMetadataByFileName(fileName: String): SDCardFileMetaDataResponse {
        return roomDb.getFileDao().getFileMetadataByFileName(fileName)
    }
}