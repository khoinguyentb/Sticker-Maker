package com.kan.dev.st051_stickermaker.data.model

import com.google.gson.annotations.SerializedName

data class StickerModel(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("category") val category: String,
    @SerializedName("link") val link: String,
    @SerializedName("name") val name: String,
    @SerializedName("vip") val vip: Int
)