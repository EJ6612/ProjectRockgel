package dev.ej6612.projectrockgel.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "checklists")
data class Checklist(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: LocalDate? = null,
    @ColumnInfo(name = "items") var items: MutableList<ChecklistItem> = mutableListOf<ChecklistItem>(),
    @ColumnInfo(name = "events") var events: MutableList<Event> = mutableListOf<Event>()
)