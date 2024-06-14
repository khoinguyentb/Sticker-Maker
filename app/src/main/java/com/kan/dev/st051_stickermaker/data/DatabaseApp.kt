package com.kan.dev.st051_stickermaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kan.dev.st051_stickermaker.data.dao.StickerPackageDao
import com.kan.dev.st051_stickermaker.data.model.StickerPackageModel
import java.util.concurrent.Executors

@Database(entities = [StickerPackageModel::class], version = 1, exportSchema = false)
abstract class DatabaseApp : RoomDatabase() {

    abstract fun StickerPackageDao() : StickerPackageDao

    companion object{
        private var INSTANCE : DatabaseApp? = null
        fun getDatabase(context: Context) : DatabaseApp{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "fake_video_call_db"
                ).addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute{

                        }
                    }
                })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}