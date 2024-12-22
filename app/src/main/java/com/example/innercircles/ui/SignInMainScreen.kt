package com.example.innercircles.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innercircles.SessionManager

enum class SignInScreen {
    SignIn,
    SignUp,
    MainScreen,
    TermsOfUseScreen,
    CompleteTermsOfUseScreen,
}
@Composable
fun SignInMainScreen(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
){
    val userUiState by userViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = SignInScreen.SignIn.name
    ) {
        composable(route = SignInScreen.SignIn.name) {
            SignInScreen(
                updateUser = {
                    userViewModel.setAttributes(it)
                },
                onClickSignIn = {
                    navController.navigate(SignInScreen.MainScreen.name)
                },
                onTouOutdated = {
                    navController.navigate(SignInScreen.TermsOfUseScreen.name)
                },
                onClickSignUp = {
                    navController.navigate(SignInScreen.SignUp.name)
                }
            )
        }
        composable(route = SignInScreen.SignUp.name) {
            SignUpScreen(
                onClickSignUp = { navController.navigate(SignInScreen.SignIn.name) },
                onClickBack = { navController.popBackStack() }
            )
        }

        composable(route = SignInScreen.TermsOfUseScreen.name) {
            TermsOfUseScreen(
                onClickBack = { navController.popBackStack() },
                onReadFullTermsOfUse = { navController.navigate(SignInScreen.CompleteTermsOfUseScreen.name) },
                onClickAccept = { navController.navigate(SignInScreen.MainScreen.name) }
            )
        }

        composable(route = SignInScreen.CompleteTermsOfUseScreen.name) {
            CompleteTermsOfUseScreen(
                onClickBack = { navController.popBackStack() },
            )
        }
        composable(route = SignInScreen.MainScreen.name) {
            MainScreen(
                userViewModel = userViewModel,
            )
        }
    }
}