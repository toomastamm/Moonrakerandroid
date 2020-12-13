package com.example.moonraker_android.ui.sd_card

import java.time.LocalDateTime

data class SDCardFileResponse(
    val error: String?,
    val filename: String,
    val modified: LocalDateTime?,
    val size: String
)