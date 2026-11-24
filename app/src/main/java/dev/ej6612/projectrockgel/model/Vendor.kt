package dev.ej6612.projectrockgel.model
import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.TypeConverter
import dev.ej6612.projectrockgel.data.local.AppDatabase
import dev.ej6612.projectrockgel.model.checklists.Checklist
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

import java.util.UUID


@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "emoji") var emoji: String = "🏢",
    @ColumnInfo(name = "phone") var phone: String = "",
    @ColumnInfo(name = "is_textable") var textable: Boolean = false,
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "notes") var notes: String = ""
)

class VendorConverters {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun eventsToJson(value: List<Event>): String =
        json.encodeToString(value)

    @TypeConverter
    fun jsonToEvents(value: String): List<Event> =
        json.decodeFromString(value)

    @TypeConverter
    fun checklistsToJson(value: List<Checklist>): String =
        json.encodeToString(value)

    @TypeConverter
    fun jsonToChecklists(value: String): List<Checklist> =
        json.decodeFromString(value)
}


object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        instance?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "vendors.db"
            ).build().also { instance = it }
        }
}

