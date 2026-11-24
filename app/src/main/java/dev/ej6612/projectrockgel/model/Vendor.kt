package dev.ej6612.projectrockgel.model
import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import dev.ej6612.projectrockgel.data.local.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.util.UUID


@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "emoji") val emoji: String = "🏢",
    @ColumnInfo(name = "phone") val phone: String = "",
    @ColumnInfo(name = "is_textable") val textable: Boolean = false,
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "events") val events: List<Event> = emptyList(),
    @ColumnInfo(name = "checklists") val checklists: List<Checklist> = emptyList(),
    @ColumnInfo(name = "notes") val notes: String = ""
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


// DAO
@Dao
interface VendorDao {
    @Query("SELECT * FROM vendors ORDER BY name")
    fun observeVendors(): Flow<List<Vendor>>

    @Query("SELECT * FROM vendors WHERE id = :id")
    suspend fun findById(id: String): Vendor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vendor: Vendor)

    @Delete
    suspend fun delete(vendor: Vendor)
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

