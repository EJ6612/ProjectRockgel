package dev.ej6612.projectrockgel.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.ej6612.projectrockgel.data.local.entity.VendorChecklistCrossRef
import dev.ej6612.projectrockgel.data.local.relation.VendorWithChecklists
import kotlinx.coroutines.flow.Flow
import dev.ej6612.projectrockgel.model.Vendor

@Dao
interface VendorDao {

    // Use this for a simple alphabetical vendor list.
    @Query("SELECT * FROM vendors ORDER BY name ASC")
    fun observeVendors(): Flow<List<Vendor>>

    // Use this when a screen needs each vendor and its linked checklists.
    @Transaction
    @Query("SELECT * FROM vendors ORDER BY name ASC")
    fun observeVendorsWithChecklists(): Flow<List<VendorWithChecklists>>

    @Transaction
    @Query("SELECT * FROM vendors WHERE id = :id")
    suspend fun findVendorWithChecklists(id: String): VendorWithChecklists?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vendor: Vendor)

    @Delete
    suspend fun delete(vendor: Vendor)

    // Adds a Vendor ↔ Checklist link.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun linkVendorToChecklist(link: VendorChecklistCrossRef)

    // Removes only the link. It does not delete the Vendor or Checklist.
    @Query(
        """
        DELETE FROM vendor_checklist_cross_ref
        WHERE vendor_id = :vendorId
        AND checklist_id = :checklistId
        """
    )
    suspend fun unlinkVendorFromChecklist(
        vendorId: String,
        checklistId: String
    )
}
