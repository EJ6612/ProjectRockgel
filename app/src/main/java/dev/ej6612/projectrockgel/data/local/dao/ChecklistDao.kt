package dev.ej6612.projectrockgel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.ej6612.projectrockgel.model.checklists.Checklist
import dev.ej6612.projectrockgel.model.checklists.ChecklistItem
import dev.ej6612.projectrockgel.model.checklists.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    // Flow emits a new list whenever checklists or their items change.
    @Transaction
    @Query("SELECT * FROM checklists ORDER BY title ASC")
    fun observeChecklists(): Flow<List<ChecklistWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(checklist: Checklist)

    @Delete
    suspend fun deleteChecklist(checklist: Checklist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItem)

    @Update
    suspend fun updateItem(item: ChecklistItem)

    @Delete
    suspend fun deleteItem(item: ChecklistItem)
}