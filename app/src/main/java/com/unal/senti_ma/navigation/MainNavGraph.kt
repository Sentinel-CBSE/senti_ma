package com.unal.senti_ma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.ui.screens.profile.ProfileScreen
import com.unal.senti_ma.ui.screens.robbery_detail.RobberyDetailScreen
import com.unal.senti_ma.ui.screens.robbery_map.RobberyMapScreen
import com.unal.senti_ma.ui.screens.robbery_search.RobberySearchScreen
import com.unal.senti_ma.utils.navigateSingleTopTo

@Composable
fun MainNavGraph(
    user: User,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RobberyMap.route,
        modifier = modifier
    ) {
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
            if (robberyId != null) {
                RobberyDetailScreen(robberyId = robberyId)
            }
        }

        composable(route = Profile.route) {
            ProfileScreen(
                user = user,
            )
        }
    }
}
