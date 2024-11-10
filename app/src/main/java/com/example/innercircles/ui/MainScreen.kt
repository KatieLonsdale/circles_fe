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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.innercircles.SessionManager
import com.example.innercircles.api.data.CommentViewModel
import com.example.innercircles.api.data.PostViewModel
import com.example.innercircles.ui.notifications.NotificationsScreen


enum class InnerCirclesScreen {
    Circle,
    NewPost,
    SelectCircles,
    DisplayPost,
    SignIn,
}


@Composable
fun MainScreen(
    circleViewModel: CircleViewModel = viewModel(),
    newPostViewModel: NewPostViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel(),
    commentViewModel: CommentViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
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
                        icon = {
                            Icon(
                                painterResource(screen.iconResourceId),
                                contentDescription = null
                            )
                        },
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
        val postUiState by postViewModel.uiState.collectAsState()
        val commentUiState by commentViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screen.Newsfeed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Newsfeed.route) {
                NewsfeedScreen(
                    onClickDisplayPost = {
                        postViewModel.resetPost()
                        postViewModel.setCurrentPost(it)
                        navController.navigate(InnerCirclesScreen.DisplayPost.name)
                    }
                )
            }
            composable(Screen.MyCircles.route) {
                MyCirclesScreen(
                    onCircleClick = {
                        circleViewModel.setCurrentCircle(it)
                        navController.navigate(InnerCirclesScreen.Circle.name)
                    }
                )
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    logOutUser = {
                        SessionManager.clearSession()
                        navController.navigate(InnerCirclesScreen.SignIn.name)
                    }
                )
            }
            // Routes without icons (not in nav bar)
            composable(route = InnerCirclesScreen.Circle.name) {
                CircleScreen(
                    circle = circleUiState,
                    onClickBack = { navController.popBackStack() },
                    onClickDisplayPost = {
                        postViewModel.resetPost()
                        postViewModel.setCurrentPost(it)
                        navController.navigate(InnerCirclesScreen.DisplayPost.name)
                    }
                )
            }
            composable(route = InnerCirclesScreen.SelectCircles.name) {
                SelectCirclesScreen(
                    newPostUiState = newPostUiState,
                    setCircles = { newPostViewModel.setCircleIds(it) },
                    onClickPost = {
                        newPostViewModel.resetNewPost()
                        navController.navigate(Screen.Newsfeed.route)
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

            composable(route = InnerCirclesScreen.DisplayPost.name) {
                DisplayPostScreen(
                    post = postUiState,
                    comment = commentUiState,
                    onClickBack = { navController.popBackStack() },
                    onCommentChanged = { commentViewModel.setCommentText(it) },
                    addCommentToPost = { postViewModel.addComment(it) },
                    clearComment = { commentViewModel.resetComment() }
                )
            }

            composable(route = InnerCirclesScreen.SignIn.name) {
                SignInMainScreen()
            }
        }
    }
}


sealed class Screen(val route: String, val iconResourceId: Int) {
    object Newsfeed : Screen("Newsfeed", R.drawable.ic_home_black_24dp)
    object MyCircles : Screen("My Circles", R.drawable.ic_my_circles_black_24dp)
    object Notifications : Screen("Notifications", R.drawable.ic_notifications_black_24dp)
}

//todo: nav bar should not be visible when user is not signed or signed up

