package com.unal.senti_ma.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface TabBarDestination {
    val route: String
    val selectedIcon: ImageVector
    val unSelectedIcon: ImageVector
}

data object Profile : TabBarDestination {
    override val route = "profile"
    override val selectedIcon = Icons.Filled.Person
    override val unSelectedIcon = Icons.Outlined.PersonOutline
}

data object Robbery : TabBarDestination {
    override val route = "robbery"
    override val selectedIcon = Icons.Filled.Map
    override val unSelectedIcon = Icons.Outlined.Map
}

val tabBarScreens = listOf(Profile, Robbery)
