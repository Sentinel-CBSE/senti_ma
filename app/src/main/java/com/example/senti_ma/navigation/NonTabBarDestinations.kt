package com.example.senti_ma.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NonTabBarDestination {
    val route: String
}

data object Login : NonTabBarDestination {
    override val route = "login"
}

data object SignUp : NonTabBarDestination {
    override val route = "sign_up"
}

data object ForgotPassword : NonTabBarDestination {
    override val route = "forgot_password"
}

data object RobberyDetail : NonTabBarDestination {
    override val route = "robbery_detail"

    const val ID_ARG = "id"
    const val ROUTE_WITH_ARGS = "robbery_detail?${ID_ARG}={${ID_ARG}}"
    val arguments = listOf(
        navArgument(ID_ARG) { type = NavType.StringType }
    )
}
