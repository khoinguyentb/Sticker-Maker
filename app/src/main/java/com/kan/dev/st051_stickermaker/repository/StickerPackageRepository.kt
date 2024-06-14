package com.kan.dev.st051_stickermaker.repository

import androidx.annotation.WorkerThread
import com.kan.dev.st051_stickermaker.data.dao.StickerPackageDao
import com.kan.dev.st051_stickermaker.data.model.StickerPackageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StickerPackageRepository @Inject constructor(
    private val dao: StickerPackageDao
) {

    fun getAll(): Flow<List<StickerPackageModel>> {
        return dao.getAll()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getListPackageName(): Flow<List<String>> {
        return withContext(Dispatchers.IO){
            dao.getListPackageName()
        }
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getItem(id: Int) :  StickerPackageModel {
        return withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(stickerPackageModel: StickerPackageModel) {
        withContext(Dispatchers.IO) {
            dao.insert(stickerPackageModel)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(stickerPackageModel: StickerPackageModel) {
        withContext(Dispatchers.IO) {
            dao.update(stickerPackageModel)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(stickerPackageModel: StickerPackageModel) {
        withContext(Dispatchers.IO) {
            dao.delete(stickerPackageModel)
        }
    }

}