package dev.ej6612.projectrockgel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "checklistItems")
data class ChecklistItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "checked") var isChecked: Boolean = false,
    @ColumnInfo(name = "parent") var parentId: String
) {

    fun toggleItemMark() {
        isChecked = !isChecked
    }

    fun isItemChecked(): Boolean = isChecked
}
