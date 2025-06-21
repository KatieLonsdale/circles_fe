package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.katielonsdale.chatterbox.ui.mycircles.MyCirclesScreen
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import com.katielonsdale.chatterbox.api.data.PostViewModel
import com.katielonsdale.chatterbox.ui.notifications.NotificationsScreen
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.data.viewModels.NotificationViewModel
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.TopAppBar
import com.katielonsdale.chatterbox.ui.components.TopAppBarNoNav

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
    EditUser
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    route: String?,
    circleId: String?,
    postId: String?,
    onRequestNotificationPermission: () -> Unit
) {
    val isUserLoggedIn by SessionManager.isUserLoggedIn.collectAsState()
    val isTouUpToDate by SessionManager.isTouUpToDate.collectAsState()
    val navController: NavHostController = rememberNavController()
    val circleViewModel: CircleViewModel = viewModel()
    val newPostViewModel: NewPostViewModel = viewModel()
    val postViewModel: PostViewModel = viewModel()
    val commentViewModel: CommentViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val allowFullAccess = isUserLoggedIn && isTouUpToDate
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(
        isUserLoggedIn
    ){
        if (!isUserLoggedIn) {
            navController.navigate(InnerCirclesScreen.SignIn.name) {
                popUpTo(0) // Clear the back stack
            }
        } else {
            val userId = SessionManager.getUserId()
            if (route == "display_post" && postId != null && circleId != null) {
                userViewModel.getUser(userId)
                postViewModel.getPost(
                    postId = postId,
                    circleId = circleId,
                )
                navController.navigate(InnerCirclesScreen.DisplayPost.name)
            } else {
                userViewModel.getUser(userId)
            }
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeContent,
        topBar = {
            TopBarGenerator(
                navController = navController,
                scrollBehavior = scrollBehavior,
                loggedIn = isUserLoggedIn,
            )
        },
        bottomBar = {
            if (allowFullAccess) {
               NavBar(
                   currentDestination = currentDestination,
                   navController = navController,
               )
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Screen.MyCircles.route || currentDestination?.route == InnerCirclesScreen.Circle.name) {
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
            startDestination = Screen.MyCircles.route,
            modifier = Modifier.padding(innerPadding)
        ) {
//            composable(Screen.Newsfeed.route) {
//                NewsfeedScreen(
//                    onClickDisplayPost = {
//                        postViewModel.resetPost()
//                        postViewModel.setCurrentPost(it)
//                        navController.navigate(InnerCirclesScreen.DisplayPost.name)
//                    }
//                )
//            }
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
                    onRequestNotificationPermission = onRequestNotificationPermission
                )
            }
            composable(Screen.Me.route) {
                MeScreen(
                    currentUser = userViewModel.getCurrentUser(),
                    onClickEdit = {
                        navController.navigate(InnerCirclesScreen.EditUser.name)
                    },
                    onClickLogOut = {
                        SessionManager.clearSession()
                        navController.navigate(InnerCirclesScreen.SignIn.name)
                    },
                )
            }
            // Routes without icons (not in nav bar)
            composable(route = InnerCirclesScreen.Circle.name) {
                CircleScreen(
                    circle = circleUiState,
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
                        navController.navigate(Screen.MyCircles.route)
                    },
                )
            }
            composable(route = InnerCirclesScreen.NewPost.name) {
                NewPostScreen(
                    circleId = circleUiState.id,
                    newPostUiState = newPostUiState,
                    onCaptionChanged = { newPostViewModel.setCaption(it) },
                    onMediaSelected = { newPostViewModel.setContent(it) },
                    onClickNext = {navController.navigate(InnerCirclesScreen.SelectCircles.name)},
                )
            }

            composable(route = InnerCirclesScreen.NewCircle.name) {
                NewCircleScreen(
                    onClickCreate = { navController.navigate(Screen.MyCircles.route) }
                )
            }

            composable(route = InnerCirclesScreen.DisplayPost.name) {
                DisplayPostScreen(
                    post = postUiState,
                    comment = commentUiState,
                    onCommentChanged = { commentViewModel.setCommentText(it) },
                    addCommentToPost = { postViewModel.addComment(it) },
                    clearComment = { commentViewModel.resetComment() }
                )
            }

            composable(route = InnerCirclesScreen.CreateNew.name) {
                CreateNewScreen(
                    onClickNewPost = { navController.navigate(InnerCirclesScreen.NewPost.name) },
                    onClickNewCircle = { navController.navigate(InnerCirclesScreen.NewCircle.name) },
                    onClickNewFriend = { navController.navigate(InnerCirclesScreen.AddFriend.name) }
                )
            }

            composable(route = InnerCirclesScreen.EditUser.name) {
                EditUserScreen()
            }
            //todo: add adding friend functionality
//            composable(route = InnerCirclesScreen.AddFriend.name) {
//                AddFriendScreen(
//                    onNavigateToNewsfeed = { navController.navigate(Screen.Newsfeed.route) }
//                )
//            }

            composable(route = InnerCirclesScreen.SignIn.name) {
                SignInScreen(
                    updateUser = {
                        userViewModel.setCurrentUser(it)
                    },
                    onClickSignIn = { navController.navigate(Screen.MyCircles.route) },
                    onTouOutdated = {
                        navController.navigate(InnerCirclesScreen.TermsOfUseScreen.name)
                    },
                    onClickSignUp = {
                        navController.navigate(InnerCirclesScreen.SignUp.name)
                    },
                )
            }
            composable(route = InnerCirclesScreen.SignUp.name) {
                SignUpScreen(
                    onClickSignUp = {
                        navController.navigate("sign_in?signedUp=true") {
                            popUpTo("sign_up") { inclusive = true }
                        }
                    },
                )
            }

            // special route for after a successful user sign up
            composable(
                route = "sign_in?signedUp={signedUp}",
                arguments = listOf(
                    navArgument("signedUp") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val signedUp = backStackEntry.arguments?.getBoolean("signedUp") ?: false
                SignInScreen(
                    updateUser = { userViewModel.setCurrentUser(it) },
                    onClickSignIn = { navController.navigate(Screen.MyCircles.route) },
                    onTouOutdated = { navController.navigate(InnerCirclesScreen.TermsOfUseScreen.name) },
                    onClickSignUp = { navController.navigate(InnerCirclesScreen.SignUp.name) },
                    signedUp = signedUp
                )
            }

            composable(route = InnerCirclesScreen.TermsOfUseScreen.name) {
                TermsOfUseScreen(
                    onReadFullTermsOfUse = { navController.navigate(InnerCirclesScreen.CompleteTermsOfUseScreen.name) },
                    onClickAccept = { navController.navigate(Screen.MyCircles.route) },
                )
            }

            composable(route = InnerCirclesScreen.CompleteTermsOfUseScreen.name) {
                CompleteTermsOfUseScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarGenerator(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    loggedIn: Boolean,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val homeScreens = arrayOf(
        Screen.MyCircles.route,
        Screen.Notifications.route,
        Screen.Me.route
    )

    if (currentDestination?.route != InnerCirclesScreen.SignIn.name) {
        if (homeScreens.contains(currentDestination?.route)) {
            TopAppBarNoNav(
                scrollBehavior = scrollBehavior,
            )
        } else {
            TopAppBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                loggedIn = loggedIn,
            )
        }
    }
}

@Composable
fun NavBar(
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = Dp.Hairline,
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = (0.5F)
                )
            )
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 100.dp
        ) {
            listOf(
                Screen.MyCircles,
                Screen.Notifications,
                Screen.Me,
            ).forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(screen.iconResourceId),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = screen.route,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = (0.3F)
                        ),
                        disabledIconColor = MaterialTheme.colorScheme.secondary,
                        disabledTextColor = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNavBar(){
    val navController: NavHostController = rememberNavController()
    ChatterBoxTheme {
        NavBar(
            currentDestination = null,
            navController = navController,
        )
    }
}


sealed class Screen(val route: String, val iconResourceId: Int) {
    data object MyCircles : Screen("My Chatters", R.drawable.my_chatters_nav)
    data object Notifications : Screen("Notifications", R.drawable.notifications_nav)
    data object Me : Screen("Me", R.drawable.me_nav)
}

