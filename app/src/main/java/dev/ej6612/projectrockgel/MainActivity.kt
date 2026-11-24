package dev.ej6612.projectrockgel

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.ej6612.projectrockgel.viewmodel.CalendarViewModel
import dev.ej6612.projectrockgel.ui.screens.ChecklistsScreen
import dev.ej6612.projectrockgel.ui.screens.ProfileScreen
import dev.ej6612.projectrockgel.ui.screens.VendorsScreen
import dev.ej6612.projectrockgel.ui.theme.ProjectRockgelTheme
import dev.ej6612.projectrockgel.ui.theme.ThemeMode
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.ej6612.projectrockgel.viewmodel.ChecklistsViewModel
import dev.ej6612.projectrockgel.viewmodel.VendorsViewModel
import dev.ej6612.projectrockgel.ui.screens.CalendarScreen

// Destinations
private const val ROUTE_VENDORS = "vendors"
private const val ROUTE_CALENDAR = "calendar"
private const val ROUTE_CHECKLISTS = "checklists"
private const val ROUTE_PROFILE = "profile"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.System) }

            ProjectRockgelTheme (themeMode = themeMode) {
                AppContent(
                    themeMode = themeMode,
                    onThemeModeChange = { themeMode = it }
                )
            }
        }
    }
}

@Composable
fun AppContent(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val checklistsViewModel: ChecklistsViewModel = viewModel()
    val vendorsViewModel: VendorsViewModel = viewModel()
    val calendarViewModel: CalendarViewModel = viewModel()

    Scaffold(
        bottomBar = {
            @OptIn(ExperimentalLayoutApi::class)
            if (!WindowInsets.isImeVisible) {
                NavigationBar {
                    val items = listOf(
                        Triple(
                            ROUTE_CALENDAR,
                            "Schedule",
                            Icons.Outlined.CalendarToday to Icons.Filled.CalendarToday
                        ),
                        Triple(
                            ROUTE_VENDORS,
                            "Vendors",
                            Icons.Outlined.Store to Icons.Filled.Store
                        ),
                        Triple(
                            ROUTE_CHECKLISTS,
                            "Checklists",
                            Icons.Outlined.CheckCircle to Icons.Filled.CheckCircle
                        ),
                        Triple(
                            ROUTE_PROFILE,
                            "Profile",
                            Icons.Outlined.Person to Icons.Filled.Person
                        ),
                    )

                    items.forEach { (itemRoute, label, icons) ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == itemRoute } == true
                        val (unselectedIcon, selectedIcon) = icons
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(label) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(itemRoute) {
                                    currentDestination?.let { destination ->
                                        if (destination.hierarchy.any { it.route == navController.graph.startDestinationRoute }) {
                                            popUpTo(destination.id) {
                                                saveState = true
                                            }
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_CALENDAR,
            modifier = Modifier.padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(ROUTE_CALENDAR) {
                CalendarScreen()
            }

            composable(ROUTE_VENDORS) {
                VendorsScreen(
                    viewModel = vendorsViewModel,
                    availableChecklists = checklistsViewModel.checklists
                )
            }
            composable(ROUTE_CHECKLISTS) {
                ChecklistsScreen(viewModel = checklistsViewModel, modifier = Modifier) }
            composable(ROUTE_PROFILE) {
                ProfileScreen(
                    currentThemeMode = themeMode,
                    onThemeModeChange = onThemeModeChange
                )
            }
        }
    }
}