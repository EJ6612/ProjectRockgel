package dev.ej6612.projectrockgel.model.checklists

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.UUID
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "checklists")
data class Checklist(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: LocalDate? = null
)

class DateConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let(LocalDate::parse)
}

data class ChecklistWithItems(
    @Embedded
    val checklist: Checklist,

    @Relation(
        parentColumn = "id",
        entityColumn = "parent_id"
    )
    val items: List<ChecklistItem>
)
