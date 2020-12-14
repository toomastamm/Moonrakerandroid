package com.example.moonraker_android.ui.sd_card

import android.app.Application
import android.util.Log
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

    fun loadFiles() { // WORKS
        Log.d("SDCardAPI", "SDCardViewModel, loadFiles")
        SDCardAPI.getFiles(_state)
    }

    fun loadFileDetails(fileName: String) { // WORKS
        Log.d("SDCardAPI", "SDCardViewModel, loadFileDetails, fileName: $fileName")
        SDCardAPI.getFileMetadata(_metaData, fileName)
    }

    fun getFileByName(fileName: String): SDCardFileResponse { // 1 WORKS
        Log.d("SDCardAPI", "SDCardViewModel, getFileByName, fileName: $fileName")
        return roomDb.getFileDao().getFileByName(fileName)
    }

    fun getFileNames(): ArrayList<String> { // WORKS
        Log.d("SDCardAPI", "SDCardViewModel, getFileNames")
        return ArrayList(roomDb.getFileDao().getFileNames().toList())
    }

    fun addFile(file: SDCardFileResponse) { // WORKS
        Log.d("SDCardAPI", "SDCardViewModel, addFile, fileName: $file")
        roomDb.getFileDao().insertFile(file)
    }

    fun addFileMetadata(metadata: SDCardFileMetaDataResponse) { // DONT WORK
        Log.d("SDCardAPI", "SDCardViewModel, addFileMetaData, metaData: $metadata")
        roomDb.getFileDao().insertFileMetaData(metadata)
    }

    fun getFileMetadataByFileName(fileName: String): SDCardFileMetaDataResponse { // 1 WORKS
        Log.d("SDCardAPI", "SDCardViewModel, getFileMetadataByFileName, fileName: $fileName")
        return roomDb.getFileDao().getFileMetadataByFileName(fileName)
    }

    fun clearFileMetadata() {
        roomDb.getFileDao().clearFileMetadata()
    }
}