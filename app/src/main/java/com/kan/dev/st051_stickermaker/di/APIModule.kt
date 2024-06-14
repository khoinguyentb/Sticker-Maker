package com.kan.dev.st051_stickermaker.di

import com.kan.dev.st051_stickermaker.api.StickerApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://lvtglobal.site/api/sticker/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStickerApiService(retrofit: Retrofit): StickerApiService {
        return retrofit.create(StickerApiService::class.java)
    }
}