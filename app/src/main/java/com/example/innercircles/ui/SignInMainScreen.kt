package com.example.innercircles.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innercircles.SessionManager

enum class SignInScreen {
    SignIn,
    SignUp,
    MainScreen,
}
@Composable
fun SignInMainScreen(
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = SignInScreen.SignIn.name
    ) {
        composable(route = SignInScreen.SignIn.name) {
            SignInScreen(
                onClickSignIn = {
                    navController.navigate(SignInScreen.MainScreen.name)
                },
                onClickSignUp = {
                    navController.navigate(SignInScreen.SignUp.name)
                }
            )
        }
        composable(route = SignInScreen.SignUp.name) {
            SignUpScreen(
                onClickSignUp = { navController.navigate(SignInScreen.MainScreen.name) },
                onClickBack = { navController.popBackStack() }
            )
        }
        composable(route = SignInScreen.MainScreen.name) {
            MainScreen()
        }
    }
}