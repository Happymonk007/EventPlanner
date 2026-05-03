package com.example.eventplanner.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eventplanner.ui.bookmarks.BookmarksScreen
import com.example.eventplanner.ui.details.EventDetailsScreen
import com.example.eventplanner.ui.events.EventsScreen

const val NAV_ARG_EVENT_ID = "eventId"

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = setOf(Route.Events.route, Route.Bookmarks.route)
    val shouldShowBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    BottomNavItem.entries.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon(), contentDescription = null) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Events.route,
            modifier = Modifier,
        ) {
            composable(Route.Events.route) {
                EventsScreen(
                    contentPadding = innerPadding,
                    onOpenDetails = { eventId -> navController.navigate(Route.EventDetails.create(eventId)) },
                )
            }
            composable(Route.Bookmarks.route) {
                BookmarksScreen(
                    contentPadding = innerPadding,
                    onOpenDetails = { eventId -> navController.navigate(Route.EventDetails.create(eventId)) },
                )
            }
            composable(
                route = Route.EventDetails.pattern,
                arguments = listOf(
                    navArgument(NAV_ARG_EVENT_ID) { type = NavType.StringType },
                ),
            ) {
                EventDetailsScreen(
                    contentPadding = innerPadding,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

private enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> androidx.compose.ui.graphics.vector.ImageVector,
) {
    Events(Route.Events.route, "Events", { Icons.Filled.Event }),
    Bookmarks(Route.Bookmarks.route, "Bookmarks", { Icons.Filled.Bookmark }),
}

private sealed class Route(val route: String) {
    data object Events : Route("events")
    data object Bookmarks : Route("bookmarks")
    data object EventDetails : Route("event/{$NAV_ARG_EVENT_ID}") {
        const val ARG_EVENT_ID = NAV_ARG_EVENT_ID
        val pattern: String = route
        fun create(eventId: String): String = "event/$eventId"
    }
}

