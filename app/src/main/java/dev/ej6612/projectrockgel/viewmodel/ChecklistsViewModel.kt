package dev.ej6612.projectrockgel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import dev.ej6612.projectrockgel.model.Checklist

class ChecklistsViewModel : ViewModel() {
    val checklists = mutableStateListOf<Checklist>()

    // In ChecklistsViewModel
    fun addChecklist(title: String): Checklist? {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isEmpty()) return null

        val checklist = Checklist(title = cleanedTitle)
        checklists.add(checklist)
        return checklist
    }

    fun deleteChecklist(checklist: Checklist) {
        checklists.remove(checklist)
    }

}