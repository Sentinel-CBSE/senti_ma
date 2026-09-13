package com.example.senti_ma.ui.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.senti_ma.R
import com.example.senti_ma.navigation.TabBarDestination

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    allScreens: List<TabBarDestination>,
    onTabSelected: (TabBarDestination) -> Unit,
    currentScreen: TabBarDestination
) {
    NavigationBar(
        tonalElevation = 2.dp,
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
        allScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onTabSelected(screen) },
                label = { Text(
                    text = when (screen.route) {
                        "profile" -> stringResource(R.string.label_profile)
                        "robbery_map" -> stringResource(R.string.label_robbery_map)
                        else -> stringResource(R.string.label_robbery_search)
                    },
                    style = typography.bodySmall,
                )},
                icon = { Icon(
                    imageVector = if (currentScreen == screen) {
                        screen.selectedIcon
                    }else{ screen.unSelectedIcon },
                    contentDescription = stringResource(id = R.string.description_tab_for, screen.route),
                )},
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = colorScheme.primary,
                    unselectedIconColor = colorScheme.onSurface,
                    selectedTextColor = colorScheme.primary,
                    unselectedTextColor = colorScheme.onSurface
                ),
            )
        }
    }
}
