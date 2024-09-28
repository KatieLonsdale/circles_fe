package com.example.innercircles.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel


enum class InnerCirclesScreen {
    Circle,
    NewPost,
    SelectCircles,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    circleViewModel: CircleViewModel = viewModel(),
    newPostViewModel: NewPostViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(InnerCirclesScreen.NewPost.name)
                },
                containerColor = Color.DarkGray,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add") // Replace with your preferred icon
            }
        },
        floatingActionButtonPosition = FabPosition.Center // Optional, can position FAB in the center or elsewhere
    ) { innerPadding ->
        val circleUiState by circleViewModel.uiState.collectAsState()
        val newPostUiState by newPostViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screen.Newsfeed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Newsfeed.route) { NewsfeedScreen() }
            composable(Screen.MyCircles.route) {
                MyCirclesScreen(
                    onCircleClick = {
                        circleViewModel.setCurrentCircle(it)
                        navController.navigate(InnerCirclesScreen.Circle.name)
                    }
                )
            }
            composable(Screen.Notifications.route) { NotificationsScreen() }
            // Routes without icons (not in nav bar)
            composable(route = InnerCirclesScreen.Circle.name) {
                CircleScreen(
                    circleId = circleUiState.id,
                    onClickBack = { navController.popBackStack() }
                )
            }
            composable(route = InnerCirclesScreen.SelectCircles.name) {
                SelectCirclesScreen(
                    newPostUiState = newPostUiState,
                    setCircles = { newPostViewModel.setCircleIds(it) },
                    onClickPost = {
                        navController.navigate(Screen.Newsfeed.route)
                        newPostViewModel.resetNewPost()
                    },
                    onClickBack = { navController.popBackStack() }
                )
            }
            composable(route = InnerCirclesScreen.NewPost.name) {
                NewPostScreen(
                    circleId = circleUiState.id,
                    newPostUiState = newPostUiState,
                    onCaptionChanged = { newPostViewModel.setCaption(it) },
                    onMediaSelected = { newPostViewModel.setContent(it) },
                    onClickNext = {navController.navigate(InnerCirclesScreen.SelectCircles.name)},
                    onClickBack = { navController.popBackStack() }
                )
            }
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


