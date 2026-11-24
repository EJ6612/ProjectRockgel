package dev.ej6612.projectrockgel.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import dev.ej6612.projectrockgel.ui.theme.ThemeMode

@Composable
fun ProfileScreen(
    currentThemeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Profile Page",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Select Theme",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        ThemeMode.entries.forEach { mode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (mode == currentThemeMode),
                    onClick = { onThemeModeChange(mode) }
                )
                Text(
                    text = mode.name,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        currentThemeMode = ThemeMode.System,
        onThemeModeChange = {}
    )
}
