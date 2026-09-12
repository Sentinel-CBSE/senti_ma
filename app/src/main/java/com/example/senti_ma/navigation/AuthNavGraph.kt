package com.example.senti_ma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.senti_ma.ui.screens.forgotPassword.ForgotPasswordScreen
import com.example.senti_ma.ui.screens.login.LoginScreen
import com.example.senti_ma.ui.screens.signUp.SignUpScreen
import com.example.senti_ma.utils.navigateSingleTopTo

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login.route) {
        composable(route = Login.route) {
            LoginScreen(
                onSignUpClick = { navController.navigateSingleTopTo(SignUp.route) },
                onForgotPasswordClick = { navController.navigateSingleTopTo(ForgotPassword.route) },
            )
        }

        composable(route = SignUp.route) {
            SignUpScreen(
                handleLoginNavigation = { navController.navigateSingleTopTo(Login.route) },
            )
        }

        composable(route = ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackPressed = { navController.navigateSingleTopTo(Login.route) },
                onSignUpClick = { navController.navigateSingleTopTo(SignUp.route) }
            )
        }
    }
}
