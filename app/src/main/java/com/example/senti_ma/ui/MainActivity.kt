package com.example.senti_ma.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.senti_ma.R
import com.example.senti_ma.navigation.AppNavHost
import com.example.senti_ma.navigation.Profile
import com.example.senti_ma.navigation.RobberyMap
import com.example.senti_ma.navigation.tabBarScreens
import com.example.senti_ma.ui.screens.login.LoginUiState
import com.example.senti_ma.ui.screens.login.LoginViewModel
import com.example.senti_ma.ui.screens.login.events.LoginUiEvent
import com.example.senti_ma.ui.settings.ThemeViewModel
import com.example.senti_ma.ui.shared.AppBottomBar
import com.example.senti_ma.ui.shared.AppTopBar
import com.example.senti_ma.ui.theme.Senti_maTheme
import com.example.senti_ma.utils.navigateSingleTopTo
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // osmdroid requires this configuration before using any MapView
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            Senti_maTheme(darkTheme = isDarkTheme) {
                val loginViewModel: LoginViewModel = hiltViewModel()
                val loginUiState by loginViewModel.uiState.collectAsState()
                val mainViewModel: MainViewModel = hiltViewModel()
                val mainUiState by mainViewModel.uiState.collectAsState()

                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    mainViewModel.loadUserData()
                }

                LaunchedEffect(loginUiState) {
                    if (loginUiState is LoginUiState.Success) {
                        loginViewModel.onEvent(LoginUiEvent.ClearState)
                        mainViewModel.loadUserData()
                    }
                }

                when {
                    mainUiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else -> {
                        Scaffold(
                            bottomBar = {
                                if (mainUiState.user != null) {
                                    val currentBackStack by navController.currentBackStackEntryAsState()
                                    val currentRoute = currentBackStack?.destination?.route

                                    if (tabBarScreens.any { it.route == currentRoute }) {
                                        val currentTabBarScreen =
                                            tabBarScreens.first { it.route == currentRoute }
                                        AppBottomBar(
                                            allScreens = tabBarScreens,
                                            onTabSelected = { newScreen ->
                                                navController.navigateSingleTopTo(newScreen.route)
                                            },
                                            currentScreen = currentTabBarScreen
                                        )
                                    }
                                }
                            },
                            topBar = {
                                if (mainUiState.user != null) {
                                    AppTopBar(
                                        avatarUrl = mainUiState.user!!.photoUrl,
                                        onAvatarClick = { navController.navigateSingleTopTo(Profile.route) },
                                        onHomeClick = { navController.navigateSingleTopTo(RobberyMap.route) },
                                        modifier = Modifier.background(colorScheme.surfaceVariant)
                                    )
                                }
                            }
                        ) { innerPadding ->
                            AppNavHost(
                                user = mainUiState.user,
                                onSignOut = {
                                    mainViewModel.signOut()
                                    navController.navigate("auth") {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                },
                                loginViewModel = loginViewModel,
                                navController = navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
