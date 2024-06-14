package com.kan.dev.st051_stickermaker.api

import com.kan.dev.st051_stickermaker.data.model.StickerModel
import com.kan.dev.st051_stickermaker.data.model.TypeStickerModel
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path

interface StickerApiService {
    @GET("edit")
    suspend fun getSticker(
    ): Response<List<TypeStickerModel>>

}