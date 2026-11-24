package dev.ej6612.projectrockgel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import dev.ej6612.projectrockgel.data.local.dao.*
import dev.ej6612.projectrockgel.data.local.entity.VendorChecklistCrossRef
import dev.ej6612.projectrockgel.model.Vendor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VendorsViewModel(
    private val vendorDao: VendorDao
) : ViewModel() {

    // Basic vendor list for a vendor-list screen.
    val vendors: StateFlow<List<Vendor>> =
        vendorDao.observeVendors()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun saveVendor(vendor: Vendor) {
        viewModelScope.launch {
            vendorDao.save(vendor)
        }
    }

    fun deleteVendor(vendor: Vendor) {
        viewModelScope.launch {
            vendorDao.delete(vendor)
        }
    }

    fun linkVendorToChecklist(
        vendorId: String,
        checklistId: String
    ) {
        viewModelScope.launch {
            vendorDao.linkVendorToChecklist(
                VendorChecklistCrossRef(
                    vendor_id = vendorId,
                    checklist_id = checklistId
                )
            )
        }
    }

    fun unlinkVendorFromChecklist(
        vendorId: String,
        checklistId: String
    ) {
        viewModelScope.launch {
            vendorDao.unlinkVendorFromChecklist(
                vendorId = vendorId,
                checklistId = checklistId
            )
        }
    }
}


class VendorsViewModelFactory(
    private val vendorDao: VendorDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VendorsViewModel::class.java)) {
            return VendorsViewModel(vendorDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


