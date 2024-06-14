package com.kan.dev.st051_stickermaker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kan.dev.st051_stickermaker.data.model.StickerPackageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface StickerPackageDao  {
    @Query("SELECT * FROM package")
    fun getAll(): Flow<List<StickerPackageModel>>
    @Query("select Distinct packageName from package")
    fun getListPackageName() : Flow< List<String>>
    @Query("select * from package where id = :id")
    fun getItem(id: Int): StickerPackageModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stickerPackageModel: StickerPackageModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(stickerPackageModel: StickerPackageModel)

    @Delete
    fun delete(stickerPackageModel: StickerPackageModel)

}