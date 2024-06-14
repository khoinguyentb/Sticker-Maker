package com.kan.dev.st051_stickermaker.data.model

data class PhotoModel(
    val path : String,
    var active : Boolean = false,
    val duration : Long = 0L
)
