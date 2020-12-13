package com.example.moonraker_android.ui.sd_card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sd_card_file_metadata")
data class SDCardFileMetaDataResponse(
    @PrimaryKey
    @ColumnInfo(name = "filename")
    val fileName: String,

    val error: String?,

    @ColumnInfo(name = "estimatedtime")
    val estimatedTime: String,

    @ColumnInfo(name = "slicername")
    val slicerName: String,

    @ColumnInfo(name = "slicerversion")
    val slicerVersion: String
)