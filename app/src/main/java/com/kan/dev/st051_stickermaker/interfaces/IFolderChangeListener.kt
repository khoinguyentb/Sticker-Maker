package com.kan.dev.st051_stickermaker.interfaces

import com.kan.dev.st051_stickermaker.data.model.PhotoModel

interface IFolderChangeListener {
    fun iChangeFolderClick(images : List<PhotoModel>,name: String)
}