package com.kriahsnverma.securevault.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DashboardBottomBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onSecondary,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home" ) },
            label = { Text("Home")},
            selected = true,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Key, contentDescription = "Password Generator" ) },
            label = { Text("Generator")},
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings" ) },
            label = { Text("Settings")},
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    MaterialTheme {
        DashboardBottomBar()
    }
}