package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.katielonsdale.chatterbox.ui.mycircles.MyCirclesScreen
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.data.PostViewModel
import com.katielonsdale.chatterbox.ui.notifications.NotificationsScreen
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.data.viewModels.NotificationViewModel
import com.katielonsdale.chatterbox.ui.components.CreateNewFloatingActionButton
import com.katielonsdale.chatterbox.ui.components.NavBar
import com.katielonsdale.chatterbox.ui.components.TopAppBar
import com.katielonsdale.chatterbox.ui.components.TopAppBarNoNav

enum class InnerCirclesScreen {
    Chatter,
    NewPost,
    NewChatter,
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
            if (currentDestination?.route == Screen.MyCircles.route || currentDestination?.route == InnerCirclesScreen.Chatter.name) {
                CreateNewFloatingActionButton(
                    navController = navController
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center // Optional, can position FAB in the center or elsewhere
    ) { innerPadding ->
        val circleUiState by circleViewModel.uiState.collectAsState()
        val newPostUiState by newPostViewModel.uiState.collectAsState()

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
                        navController.navigate(InnerCirclesScreen.Chatter.name)
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
            composable(route = InnerCirclesScreen.Chatter.name) {
                ChatterScreen(
                    chatter = circleUiState,
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

                )
            }
            composable(route = InnerCirclesScreen.NewPost.name) {
                NewPostScreen(
                    onCaptionChanged = { newPostViewModel.setCaption(it) },
                    onMediaSelected = { newPostViewModel.setContent(it) },
                    currentUserChatters = userViewModel.getCurrentUserChatters(),
                    onClickPost = {
                        newPostViewModel.resetNewPost()
                        navController.navigate(Screen.MyCircles.route)
                    }
                )
            }

            composable(route = InnerCirclesScreen.NewChatter.name) {
                NewCircleScreen(
                    onClickCreate = { navController.navigate(Screen.MyCircles.route) }
                )
            }

            composable(route = InnerCirclesScreen.DisplayPost.name) {
                DisplayPostScreen(
                    postViewModel = postViewModel,
                    addCommentToPost = { postViewModel.addComment(it) },
                    onFailedLoad = { navController.navigate(Screen.MyCircles.route) }
                )
            }

            composable(route = InnerCirclesScreen.CreateNew.name) {
                CreateNewScreen(
                    onClickNewPost = { navController.navigate(InnerCirclesScreen.NewPost.name) },
                    onClickNewChatter = { navController.navigate(InnerCirclesScreen.NewChatter.name) },
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

sealed class Screen(val route: String, val iconResourceId: Int) {
    data object MyCircles : Screen("My Chatters", R.drawable.my_chatters_nav)
    data object Notifications : Screen("Notifications", R.drawable.notifications_nav)
    data object Me : Screen("Me", R.drawable.me_nav)
}

