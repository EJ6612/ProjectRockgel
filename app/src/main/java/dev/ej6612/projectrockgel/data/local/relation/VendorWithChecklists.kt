package dev.ej6612.projectrockgel.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.ej6612.projectrockgel.model.Vendor
import dev.ej6612.projectrockgel.model.checklists.Checklist
import dev.ej6612.projectrockgel.data.local.entity.VendorChecklistCrossRef

data class VendorWithChecklists(
    @Embedded
    val vendor: Vendor,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = VendorChecklistCrossRef::class,
            parentColumn = "vendor_id",
            entityColumn = "checklist_id"
        )
    )
    val checklists: List<Checklist>
)
