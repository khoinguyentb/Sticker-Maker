package com.kan.dev.st051_stickermaker.di

import android.content.Context
import com.kan.dev.st051_stickermaker.data.DatabaseApp
import com.kan.dev.st051_stickermaker.data.dao.StickerPackageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): DatabaseApp {
        return DatabaseApp.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideMessageDao(databaseApp: DatabaseApp): StickerPackageDao {
        return databaseApp.StickerPackageDao()
    }


}