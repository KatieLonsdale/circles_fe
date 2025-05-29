package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.katielonsdale.chatterbox.ui.home.NewsfeedScreen
import com.katielonsdale.chatterbox.ui.mycircles.MyCirclesScreen
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import com.katielonsdale.chatterbox.api.data.PostViewModel
import com.katielonsdale.chatterbox.ui.notifications.NotificationsScreen
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.data.viewModels.NotificationViewModel


enum class InnerCirclesScreen {
    Circle,
    NewPost,
    NewCircle,
    SelectCircles,
    DisplayPost,
    CreateNew,
    AddFriend,
    SignIn,
    SignUp,
    TermsOfUseScreen,
    CompleteTermsOfUseScreen,
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    circleViewModel: CircleViewModel = viewModel(),
    newPostViewModel: NewPostViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel(),
    commentViewModel: CommentViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    notificationViewModel: NotificationViewModel = viewModel(),
    mainActivity: com.katielonsdale.chatterbox.MainActivity? = null
) {
    val isUserLoggedIn by SessionManager.isUserLoggedIn.collectAsState()
    val isTouUpToDate by SessionManager.isTouUpToDate.collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        if (!isUserLoggedIn) {
            navController.navigate(InnerCirclesScreen.SignIn.name) {
                popUpTo(0) // Clear the back stack
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (isUserLoggedIn && isTouUpToDate) {
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
            }
        },
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (currentDestination?.route == Screen.Newsfeed.route || currentDestination?.route == InnerCirclesScreen.Circle.name) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(InnerCirclesScreen.CreateNew.name)
                    },
                    containerColor = Color.DarkGray,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
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
                    },
                    onClickPostNotification = {
                        notificationViewModel.resetNotification()
                        notificationViewModel.setCurrentNotification(it)
                        postViewModel.getPost(
                            postId = it.postId ?: "",
                            circleId = it.circleId ?: "",
                        )
                        val route = notificationViewModel.getNavigationScreen(it)
                        navController.navigate(route)
                    },
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

            composable(route = InnerCirclesScreen.NewCircle.name) {
                NewCircleScreen(
                    onClickBack = { navController.popBackStack() },
                    onClickCreate = { navController.navigate(Screen.MyCircles.route) }
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

            composable(route = InnerCirclesScreen.CreateNew.name) {
                CreateNewScreen(
                    onClickBack = { navController.popBackStack() },
                    onClickNewPost = { navController.navigate(InnerCirclesScreen.NewPost.name) },
                    onClickNewCircle = { navController.navigate(InnerCirclesScreen.NewCircle.name) },
                    onClickNewFriend = { navController.navigate(InnerCirclesScreen.AddFriend.name) }
                )
            }

            composable(route = InnerCirclesScreen.AddFriend.name) {
                AddFriendScreen(
                    onClickBack = { navController.popBackStack() },
                    onNavigateToNewsfeed = { navController.navigate(Screen.Newsfeed.route) }
                )
            }

            composable(route = InnerCirclesScreen.SignIn.name) {
                SignInScreen(
                    updateUser = {
                        userViewModel.setAttributes(it)
                    },
                    onClickSignIn = { navController.navigate(Screen.Newsfeed.route) },
                    onTouOutdated = {
                        navController.navigate(InnerCirclesScreen.TermsOfUseScreen.name)
                    },
                    onClickSignUp = {
                        navController.navigate(InnerCirclesScreen.SignUp.name)
                    },
                    mainActivity = mainActivity
                )
            }
            composable(route = InnerCirclesScreen.SignUp.name) {
                SignUpScreen(
                    onClickSignUp = { navController.navigate(InnerCirclesScreen.SignIn.name) },
                    onClickBack = { navController.popBackStack() }
                )
            }

            composable(route = InnerCirclesScreen.TermsOfUseScreen.name) {
                TermsOfUseScreen(
                    onClickBack = { navController.popBackStack() },
                    onReadFullTermsOfUse = { navController.navigate(InnerCirclesScreen.CompleteTermsOfUseScreen.name) },
                    onClickAccept = { navController.navigate(Screen.Newsfeed.route) }
                )
            }

            composable(route = InnerCirclesScreen.CompleteTermsOfUseScreen.name) {
                CompleteTermsOfUseScreen(
                    onClickBack = { navController.popBackStack() },
                )
            }
        }
    }
}


sealed class Screen(val route: String, val iconResourceId: Int) {
    object Newsfeed : Screen("Newsfeed", R.drawable.ic_home_black_24dp)
    object MyCircles : Screen("My Chatters", R.drawable.ic_my_chatters_icon_24dp)
    object Notifications : Screen("Notifications", R.drawable.notifications)
}

