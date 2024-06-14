package com.kan.dev.st051_stickermaker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "package")
data class StickerPackageModel(
    val uri : String,
    val packageName :String
): Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int = 0
}
