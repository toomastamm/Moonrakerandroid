package com.example.moonraker_android.ui.print_status

data class StatusResponse(
    val state: String,
    val filename: String,
    val message: String,
    val total_duration: Number,
    val print_duration: Number,
)