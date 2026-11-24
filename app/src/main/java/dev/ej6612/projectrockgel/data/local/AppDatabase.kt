package dev.ej6612.projectrockgel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.ej6612.projectrockgel.model.Vendor
import dev.ej6612.projectrockgel.model.VendorConverters
import dev.ej6612.projectrockgel.data.local.dao.*
import dev.ej6612.projectrockgel.model.checklists.Checklist
import dev.ej6612.projectrockgel.model.checklists.ChecklistItem
import dev.ej6612.projectrockgel.data.local.entity.VendorChecklistCrossRef
@Database(
    entities = [
        Checklist::class,
        ChecklistItem::class,
        Vendor::class,
        VendorChecklistCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun checklistDao(): ChecklistDao
    abstract fun vendorDao(): VendorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "checklists_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS vendors (
                        id TEXT NOT NULL,
                        name TEXT NOT NULL,
                        emoji TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        is_textable INTEGER NOT NULL,
                        email TEXT NOT NULL,
                        address TEXT NOT NULL,
                        notes TEXT NOT NULL,
                        PRIMARY KEY(id)
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS vendor_checklist_cross_ref (
                        vendor_id TEXT NOT NULL,
                        checklist_id TEXT NOT NULL,
                        PRIMARY KEY(vendor_id, checklist_id),
                        FOREIGN KEY(vendor_id)
                            REFERENCES vendors(id)
                            ON DELETE CASCADE,
                        FOREIGN KEY(checklist_id)
                            REFERENCES checklists(id)
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_vendor_checklist_cross_ref_checklist_id
                    ON vendor_checklist_cross_ref(checklist_id)
                    """.trimIndent()
                )
            }
        }
    }
}
