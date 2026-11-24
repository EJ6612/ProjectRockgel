package dev.ej6612.projectrockgel.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import dev.ej6612.projectrockgel.model.checklists.*
import dev.ej6612.projectrockgel.viewmodel.ChecklistsViewModel
//import dev.ej6612.projectrockgel.viewmodel.ChecklistsViewModelFactory

@Composable
fun ChecklistsScreen(
    viewModel: ChecklistsViewModel,
    modifier: Modifier = Modifier
) {
    // Step 1:
    // Collect Room data. When Room changes, Compose automatically redraws this screen.
    val checklists by viewModel.checklists.collectAsStateWithLifecycle()

    // Step 2:
    // Store only temporary text-field input in Compose state.
    // Checklist data itself belongs in Room, not mutable Compose lists.
    val newItemTitles = remember {
        mutableStateMapOf<String, String>()
    }

    var newChecklistTitle by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Checklists",
            style = MaterialTheme.typography.headlineSmall
        )

        // Step 3:
        // This is the scrollable checklist area.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (checklists.isEmpty()) {
                Text("No checklists yet. Create one below.")
            }

            // Each value contains:
            // - checklistWithItems.checklist: the Checklist entity
            // - checklistWithItems.items: its related ChecklistItem entities
            checklists.forEach { checklistWithItems ->
                val checklist = checklistWithItems.checklist
                val checklistItems = checklistWithItems.items

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = checklist.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            // Step 4:
                            // Room deletes child items too because of ForeignKey.CASCADE.
                            TextButton(
                                onClick = {
                                    viewModel.deleteChecklist(checklist)

                                    // Remove temporary text input for this checklist.
                                    newItemTitles.remove(checklist.id)
                                }
                            ) {
                                Text("Delete")
                            }
                        }

                        checklist.date?.let { date ->
                            Text("Due date: $date")
                        }

                        if (checklistItems.isEmpty()) {
                            Text("No items in this checklist yet.")
                        }

                        // Step 5:
                        // Item checked state comes directly from Room.
                        checklistItems.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.checked,
                                    onCheckedChange = {
                                        // The ViewModel updates Room.
                                        // Room emits updated data back to this screen.
                                        viewModel.toggleItem(item)
                                    }
                                )

                                Text(item.title)
                            }
                        }

                        // Step 6:
                        // Each checklist has its own "new item" text field.
                        OutlinedTextField(
                            value = newItemTitles[checklist.id].orEmpty(),
                            onValueChange = { title ->
                                newItemTitles[checklist.id] = title
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("New item") },
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val title = newItemTitles[checklist.id]
                                    .orEmpty()
                                    .trim()

                                if (title.isNotEmpty()) {
                                    // The ViewModel supplies checklist.id as parentId.
                                    viewModel.addItem(
                                        checklistId = checklist.id,
                                        title = title
                                    )

                                    // Clear only this checklist's input field.
                                    newItemTitles[checklist.id] = ""
                                }
                            },
                            enabled = !newItemTitles[checklist.id].isNullOrBlank()
                        ) {
                            Text("Add item")
                        }
                    }
                }
            }
        }

        // Step 7:
        // These controls remain outside the scrollable content.
        OutlinedTextField(
            value = newChecklistTitle,
            onValueChange = { newChecklistTitle = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("New checklist name") },
            singleLine = true
        )

        Button(
            onClick = {
                val title = newChecklistTitle.trim()

                if (title.isNotEmpty()) {
                    // Room inserts the checklist asynchronously through the ViewModel.
                    viewModel.addChecklist(title)

                    // Clear the input immediately after requesting insertion.
                    newChecklistTitle = ""
                }
            },
            enabled = newChecklistTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create checklist")
        }
    }
}
