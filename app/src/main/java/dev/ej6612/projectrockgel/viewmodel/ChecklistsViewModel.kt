package dev.ej6612.projectrockgel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dev.ej6612.projectrockgel.model.checklists.Checklist
import dev.ej6612.projectrockgel.model.checklists.ChecklistDao
import dev.ej6612.projectrockgel.model.checklists.ChecklistWithItems
import dev.ej6612.projectrockgel.model.checklists.ChecklistItem

class ChecklistsViewModel(
    private val checklistDao: ChecklistDao
) : ViewModel() {

    // Compose will receive database updates through this StateFlow.
    val checklists: StateFlow<List<ChecklistWithItems>> =
        checklistDao.observeChecklists()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addChecklist(title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return

        viewModelScope.launch {
            checklistDao.insertChecklist(
                Checklist(title = cleanedTitle)
            )
        }
    }

    fun deleteChecklist(checklist: Checklist) {
        viewModelScope.launch {
            checklistDao.deleteChecklist(checklist)
        }
    }

    fun addItem(checklistId: String, title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return

        viewModelScope.launch {
            checklistDao.insertItem(
                ChecklistItem(
                    title = cleanedTitle,
                    parentId = checklistId
                )
            )
        }
    }

    fun toggleItem(item: ChecklistItem) {
        viewModelScope.launch {
            checklistDao.updateItem(
                item.copy(checked = !item.checked)
            )
        }
    }

    fun deleteItem(item: ChecklistItem) {
        viewModelScope.launch {
            checklistDao.deleteItem(item)
        }
    }
}

// ViewModel Factory
class ChecklistsViewModelFactory(
    private val checklistDao: ChecklistDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistsViewModel::class.java)) {
            return ChecklistsViewModel(checklistDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
