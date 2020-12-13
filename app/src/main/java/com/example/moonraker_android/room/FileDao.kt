package com.example.moonraker_android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moonraker_android.ui.sd_card.SDCardFileMetaDataResponse
import com.example.moonraker_android.ui.sd_card.SDCardFileResponse

@Dao
interface FileDao {
    @Query("SELECT * FROM sd_card_file where filename LIKE :fileName")
    fun getFileByName(fileName: String): SDCardFileResponse

    @Query("SELECT filename FROM sd_card_file")
    fun getFileNames(): Array<String>

    @Query("SELECT * FROM sd_card_file_metadata where filename LIKE :fileName")
    fun getFileMetadataByFileName(fileName: String): SDCardFileMetaDataResponse

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFile(file: SDCardFileResponse)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFileMetaData(metaData: SDCardFileMetaDataResponse)
}