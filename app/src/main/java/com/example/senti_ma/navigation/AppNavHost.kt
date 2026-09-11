package com.example.senti_ma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.senti_ma.domain.model.User
import com.example.senti_ma.ui.screens.forgotPassword.ForgotPasswordScreen
import com.example.senti_ma.ui.screens.login.LoginScreen
import com.example.senti_ma.ui.screens.login.LoginViewModel
import com.example.senti_ma.ui.screens.profile.ProfileScreen
import com.example.senti_ma.ui.screens.robbery_detail.RobberyDetailScreen
import com.example.senti_ma.ui.screens.robbery_map.RobberyMapScreen
import com.example.senti_ma.ui.screens.robbery_search.RobberySearchScreen
import com.example.senti_ma.ui.screens.signUp.SignUpScreen
import com.example.senti_ma.utils.navigateSingleTopTo

@Composable
fun AppNavHost(
    user: User?,
    onSignOut: () -> Unit,
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (user == null) "auth" else "main",
        modifier = modifier
    ) {
        navigation(startDestination = Login.route, route = "auth") {
            composable(route = Login.route) {
                LoginScreen(
                    onSignUpClick = { navController.navigateSingleTopTo(SignUp.route) },
                    onForgotPasswordClick = { navController.navigateSingleTopTo(ForgotPassword.route) },
                    loginViewModel = loginViewModel
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

        navigation(startDestination = RobberyMap.route, route = "main") {
            composable(route = RobberyMap.route) {
                RobberyMapScreen()
            }

            composable(route = RobberySearch.route) {
                RobberySearchScreen(
                    onRobberyClick = { robberyId ->
                        navController.navigateSingleTopTo(
                            "${RobberyDetail.route}?${RobberyDetail.ID_ARG}=$robberyId"
                        )
                    }
                )
            }

            composable(
                route = RobberyDetail.ROUTE_WITH_ARGS,
                arguments = RobberyDetail.arguments
            ) { backStackEntry ->
                val robberyId = backStackEntry.arguments?.getString(RobberyDetail.ID_ARG)

                RobberyDetailScreen(
                    robberyId = robberyId!!
                )
            }

            composable(route = Profile.route) {
                ProfileScreen(
                    user = user!!,
                    onSignOut = onSignOut
                )
            }
        }
    }
}
