package com.kan.dev.st051_stickermaker.data.model

import com.google.gson.annotations.SerializedName

data class TypeStickerModel (
    @SerializedName("category") val category: String,
    @SerializedName("items") val listSticker: List<StickerModel>,
)