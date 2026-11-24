package dev.ej6612.projectrockgel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.ej6612.projectrockgel.data.local.AppDatabase
import dev.ej6612.projectrockgel.model.Vendor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VendorsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).vendorDao()

    val vendors: Flow<List<Vendor>> = dao.observeVendors()

    fun saveVendor(vendor: Vendor) {
        viewModelScope.launch {
            dao.save(vendor)
        }
    }

    fun deleteVendor(vendor: Vendor) {
        viewModelScope.launch {
            dao.delete(vendor)
        }
    }
}
