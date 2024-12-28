//package com.katielonsdale.chatterbox.ui
//
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//
// todo: delete?
//enum class SignInScreen {
//    SignIn,
//    SignUp,
//    TermsOfUseScreen,
//    CompleteTermsOfUseScreen,
//}
//@Composable
//fun SignInMainScreen(
//    navController: NavHostController,
//    userViewModel: UserViewModel = viewModel(),
//){
//    NavHost(
//        navController = navController,
//        startDestination = SignInScreen.SignIn.name
//    ) {
//        composable(route = SignInScreen.SignIn.name) {
//            SignInScreen(
//                updateUser = {
//                    userViewModel.setAttributes(it)
//                },
//                onClickSignIn = {
//                    navController.navigate("main") {
//                        popUpTo(0)
//                    }
//                },
//                onTouOutdated = {
//                    navController.navigate(SignInScreen.TermsOfUseScreen.name)
//                },
//                onClickSignUp = {
//                    navController.navigate(SignInScreen.SignUp.name)
//                }
//            )
//        }
//        composable(route = SignInScreen.SignUp.name) {
//            SignUpScreen(
//                onClickSignUp = { navController.navigate(SignInScreen.SignIn.name) },
//                onClickBack = { navController.popBackStack() }
//            )
//        }
//
//        composable(route = SignInScreen.TermsOfUseScreen.name) {
//            TermsOfUseScreen(
//                onClickBack = { navController.popBackStack() },
//                onReadFullTermsOfUse = { navController.navigate(SignInScreen.CompleteTermsOfUseScreen.name) },
//                onClickAccept = {
//                    navController.navigate("main") {
//                        popUpTo(0)
//                    }
//                }
//            )
//        }
//
//        composable(route = SignInScreen.CompleteTermsOfUseScreen.name) {
//            CompleteTermsOfUseScreen(
//                onClickBack = { navController.popBackStack() },
//            )
//        }
//    }
//}