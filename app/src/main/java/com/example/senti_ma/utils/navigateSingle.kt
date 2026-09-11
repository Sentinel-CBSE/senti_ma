package com.example.senti_ma.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Navigates to a route and pops up to the start destination.
 * @param route The route to navigate to.
 */
fun NavHostController.navigateSingleTopTo(route: String, forceReload: Boolean = false) = this.navigate(route) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = !forceReload
}
