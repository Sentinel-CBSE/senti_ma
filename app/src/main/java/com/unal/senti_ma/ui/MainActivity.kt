package com.unal.senti_ma.ui

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unal.senti_ma.R
import com.unal.senti_ma.navigation.AuthNavGraph
import com.unal.senti_ma.navigation.MainNavGraph
import com.unal.senti_ma.navigation.Profile
import com.unal.senti_ma.navigation.Robbery
import com.unal.senti_ma.navigation.tabBarScreens
import com.unal.senti_ma.ui.auth.AuthState
import com.unal.senti_ma.ui.auth.AuthViewModel
import com.unal.senti_ma.ui.settings.SettingsViewModel
import com.unal.senti_ma.ui.shared.AppBottomBar
import com.unal.senti_ma.ui.shared.AppTopBar
import com.unal.senti_ma.ui.shared.permissions.LocationPermissionHandler
import com.unal.senti_ma.ui.shared.permissions.NotificationPermissionHandler
import com.unal.senti_ma.ui.shared.permissions.PermissionViewModel
import com.unal.senti_ma.ui.theme.Senti_maTheme
import com.unal.senti_ma.utils.navigateSingleTopTo
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // osmdroid requires this configuration before using any MapView
        val osmConfig = Configuration.getInstance()
        osmConfig.load(
            applicationContext,
            getSharedPreferences(
                getString(R.string.app_name),
                MODE_PRIVATE
            )
        )

        osmConfig.userAgentValue =
            "Senti-MA/1.0 (com.unal.senti_ma, contact: dbustos@unal.edu.co)"

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            isDarkTheme?.let { darkTheme ->
                Senti_maTheme(darkTheme = darkTheme) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsState()

                    when (val state = authState) {
                        is AuthState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is AuthState.Unauthenticated -> {
                            AuthNavGraph()
                        }

                        is AuthState.Authenticated -> {
                            val permissionViewModel: PermissionViewModel = hiltViewModel()

                            var locationRequestCompleted by rememberSaveable {
                                mutableStateOf(false)
                            }

                            LocationPermissionHandler(
                                request = true,
                                onPermissionGranted = {
                                    permissionViewModel.onLocationPermissionResult(true)
                                    locationRequestCompleted = true
                                },
                                onPermissionDenied = {
                                    permissionViewModel.onLocationPermissionResult(false)
                                    locationRequestCompleted = true
                                }
                            )

                            NotificationPermissionHandler(
                                request = locationRequestCompleted,
                                onPermissionGranted = {},
                                onPermissionDenied = {}
                            )

                            val lifecycleOwner = LocalLifecycleOwner.current

                            DisposableEffect(lifecycleOwner) {
                                val observer = LifecycleEventObserver { _, event ->
                                    if (event == Lifecycle.Event.ON_RESUME) {
                                        permissionViewModel.refreshLocationPermission()
                                    }
                                }

                                lifecycleOwner.lifecycle.addObserver(observer)

                                onDispose {
                                    lifecycleOwner.lifecycle.removeObserver(observer)
                                }
                            }

                            val navController = rememberNavController()

                            Scaffold(
                                bottomBar = {
                                    val currentBackStack by navController.currentBackStackEntryAsState()
                                    val currentRoute = currentBackStack?.destination?.route

                                    if (tabBarScreens.any { it.route == currentRoute }) {
                                        val currentTabBarScreen =
                                            tabBarScreens.first { it.route == currentRoute }

                                        AppBottomBar(
                                            allScreens = tabBarScreens,
                                            onTabSelected = { newScreen ->
                                                navController.navigateSingleTopTo(
                                                    newScreen.route
                                                )
                                            },
                                            currentScreen = currentTabBarScreen
                                        )
                                    }
                                },
                                topBar = {
                                    AppTopBar(
                                        avatarUrl = state.user.photoUrl,
                                        onAvatarClick = { navController.navigateSingleTopTo(Profile.route) },
                                        onHomeClick = { navController.navigateSingleTopTo(Robbery.route) },
                                        modifier = Modifier.background(colorScheme.surfaceVariant)
                                    )
                                }
                            ) { innerPadding ->
                                if (locationRequestCompleted) {
                                    MainNavGraph(
                                        navController = navController,
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .padding(innerPadding)
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
