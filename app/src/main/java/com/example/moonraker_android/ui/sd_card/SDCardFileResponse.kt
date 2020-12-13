package com.example.moonraker_android.ui.sd_card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sd_card_file")
data class SDCardFileResponse(
    @PrimaryKey
    @ColumnInfo(name = "filename")
    val filename: String,

    val error: String?,

    @ColumnInfo(name = "modified")
    val modified: String,

    @ColumnInfo(name = "size")
    val size: String
)