package dev.ej6612.projectrockgel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ej6612.projectrockgel.model.Vendor
import dev.ej6612.projectrockgel.model.VendorConverters
import dev.ej6612.projectrockgel.model.VendorDao

// Database
@Database(
    entities = [Vendor::class],
    version = 1
)
@TypeConverters(VendorConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vendorDao(): VendorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vendors.db"
                ).build().also { INSTANCE = it }
            }
    }
}