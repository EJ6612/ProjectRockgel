package dev.ej6612.projectrockgel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ej6612.projectrockgel.model.Checklist
import dev.ej6612.projectrockgel.model.ChecklistItem
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.TextButton
import dev.ej6612.projectrockgel.viewmodel.ChecklistsViewModel
import kotlin.collections.addAll
import kotlin.text.set

@Composable
fun ChecklistsScreen(
    viewModel: ChecklistsViewModel,
    modifier: Modifier
) {
    // read current checklist collection from the ViewModel
    val checklists = viewModel.checklists

    /* store a mutable compose-observable list of items for each checklist
     *
     * `remember(viewModel)` recreates the map only if a different ViewModel
     * instance is supplied - it does not recreate it on every recomposition
     */
    val itemsByChecklist = remember(viewModel) {
        mutableStateMapOf<Checklist, SnapshotStateList<ChecklistItem>>().apply {

            // copy each checklist's initial items into a state-aware list
            // changes to these lists can trigger compose recomposition
            viewModel.checklists.forEach { checklist ->
                this[checklist] =
                    mutableStateListOf<ChecklistItem>().apply {
                        addAll(checklist.items)
                    }
            }
        }
    }

    // tracks whether each individual checklist item is checked
    val checkedByItem = remember {
        mutableStateMapOf<ChecklistItem, Boolean>()
    }

    // stores the current text typed into the "new item" input for each checklist
    val newItemTitles = remember {
        mutableStateMapOf<Checklist, String>()
    }

    // holds the text for a new checklist title
    val newChecklistTitle by remember {
        mutableStateOf("")
    }

    // Add column later

}