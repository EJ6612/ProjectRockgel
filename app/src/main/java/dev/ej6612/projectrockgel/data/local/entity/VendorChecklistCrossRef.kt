package dev.ej6612.projectrockgel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import dev.ej6612.projectrockgel.model.Vendor
import dev.ej6612.projectrockgel.model.checklists.Checklist


@Entity(
    tableName = "vendor_checklist_cross_ref",
    primaryKeys = ["vendor_id", "checklist_id"],
    foreignKeys = [
        ForeignKey(
            entity = Vendor::class,
            parentColumns = ["id"],
            childColumns = ["vendor_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Checklist::class,
            parentColumns = ["id"],
            childColumns = ["checklist_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        // Makes queries beginning from a checklist efficient.
        Index(value = ["checklist_id"])
    ]
)
data class VendorChecklistCrossRef(
    val vendor_id: String,
    val checklist_id: String
)
