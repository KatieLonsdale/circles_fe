package com.example.innercircles.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.innercircles.ui.home.NewsfeedScreen
import com.example.innercircles.ui.mycircles.MyCirclesScreen
import com.example.innercircles.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOf(
                    Screen.Newsfeed,
                    Screen.MyCircles,
                    Screen.Notifications
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(screen.iconResourceId), contentDescription = null) },
                        label = { Text(screen.route) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Newsfeed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Newsfeed.route) { NewsfeedScreen() }
            composable(Screen.MyCircles.route) { MyCirclesScreen() }
            composable(Screen.Notifications.route) { NotificationsScreen() }
        }
    }
}

sealed class Screen(val route: String, val iconResourceId: Int) {
    object Newsfeed : Screen("Newsfeed", R.drawable.ic_home_black_24dp)
    object MyCircles : Screen("My Circles", R.drawable.ic_my_circles_black_24dp)
    object Notifications : Screen("Notifications", R.drawable.ic_notifications_black_24dp)
}

@Composable
fun NotificationsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Notifications Screen", style = MaterialTheme.typography.headlineMedium)
    }
}