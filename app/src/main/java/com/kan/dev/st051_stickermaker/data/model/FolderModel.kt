package com.kan.dev.st051_stickermaker.data.model

import java.io.Serializable

data class FolderModel(
    val name: String,
    val images: List<PhotoModel>,
    var active : Boolean = false
) :Serializable
