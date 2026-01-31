package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.presentation.components.SecureVaultBottomNav
import com.kriahsnverma.securevault.presentation.navigation.DashboardNavGraph
import com.kriahsnverma.securevault.presentation.navigation.Screen


@Composable
fun HomeDashboardScreen(
    appNavController: NavHostController,
    vaultLockManager: VaultLockManager
) {
    val dashboardNavController = rememberNavController()
    val isUnlocked by vaultLockManager.isUnlockedFlow.collectAsState()

    LaunchedEffect(isUnlocked) {
        if (!isUnlocked) {
            appNavController.navigate(Screen.AutoUnlockScreen.route) {
                popUpTo(Screen.HomeDashboardScreen.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            SecureVaultBottomNav(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    dashboardNavController.navigate(route) {
                        popUpTo(dashboardNavController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DashboardNavGraph(
                dashboardNavController = dashboardNavController,
                appNavController = appNavController,
                onNavigateToUnlock = {
                    appNavController.navigate(Screen.UnlockScreen.route){
                        popUpTo(appNavController.graph.findStartDestination().id){
                            inclusive = true
                        }
                    }
                },
                onNavigateToAddPassword = {
                    // Use createRoute() to get the actual path instead of the template string
                    appNavController.navigate(Screen.AddPasswordScreen.createRoute())
                },
                vaultLockManager = vaultLockManager
            )
        }
    }
}
