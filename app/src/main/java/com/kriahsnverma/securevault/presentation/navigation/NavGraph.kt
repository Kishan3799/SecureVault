package com.kriahsnverma.securevault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.presentation.screens.AddPasswordScreen
import com.kriahsnverma.securevault.presentation.screens.ChangeMasterPasswordScreen
import com.kriahsnverma.securevault.presentation.screens.HomeDashboardScreen
import com.kriahsnverma.securevault.presentation.screens.OnboardingScreen
import com.kriahsnverma.securevault.presentation.screens.PasswordDetailScreen
import com.kriahsnverma.securevault.presentation.screens.PasswordGeneratorScreen
import com.kriahsnverma.securevault.presentation.screens.SetupMasterPasswordScreen
import com.kriahsnverma.securevault.presentation.screens.SplashScreen
import com.kriahsnverma.securevault.presentation.screens.UnlockScreen
import com.kriahsnverma.securevault.presentation.screens.AutoUnlockScreen
import com.kriahsnverma.securevault.presentation.screens.VaultScreen
import com.kriahsnverma.securevault.presentation.screens.VaultSettingScreen
import com.kriahsnverma.securevault.presentation.viewmodel.SplashViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

// --- 1. Cleaned up Screen definitions ---
sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object SetupMasterPasswordScreen : Screen("setup_master_password_screen")
    object UnlockScreen : Screen("unlock_screen")
    object AutoUnlockScreen : Screen("auto_unlock_screen")
    object HomeDashboardScreen : Screen("home_dashboard_screen")

    object AddPasswordScreen : Screen("add_password_screen?passwordId={passwordId}") {
        fun createRoute(passwordId: Int = -1) = "add_password_screen?passwordId=$passwordId"
    }

    // The route definition itself
    object DetailPasswordScreen : Screen("detail_password_screen/{passwordId}") {
        // A helper function to build the route with an actual ID
        fun createRoute(passwordId: Int) = "detail_password_screen/$passwordId"
    }

    object ChangeMasterPasswordScreen : Screen("change_master_password_screen")
}

// --- 2. The main entry point for your app's UI ---
@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController(),
    vaultLockManager: VaultLockManager
) {

    val splashViewModel: SplashViewModel = hiltViewModel()
    val startDestination by splashViewModel.startDestination.collectAsState()

    // Show a simple splash UI while the ViewModel decides where to go.
    // This prevents a flicker/blank screen.
    if (startDestination == null) {
        SplashScreen() // The splash screen UI is now completely dumb
        return
    }

    // --- 3. The NavHost is built ONLY after the start destination is known ---
    NavHost(
        navController = navController,
        startDestination = startDestination!! // We know it's not null here
    ) {
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(
                onNavigateToSetup = {
                    navController.navigate(Screen.SetupMasterPasswordScreen.route) {
                        popUpTo(Screen.OnboardingScreen.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.SetupMasterPasswordScreen.route) {
            SetupMasterPasswordScreen(
                onSetupComplete = {
                    // After setup, the user is "unlocked". Go to the main app screen.
                    // Clear the entire auth flow from the back stack.
                    navController.navigate(Screen.HomeDashboardScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.UnlockScreen.route) {
            UnlockScreen(
                onUnlocked = {
                    // After unlock, go to the main app screen and remove
                    // the unlock screen from the back stack.
                    navController.navigate(Screen.HomeDashboardScreen.route) {
                        popUpTo(Screen.UnlockScreen.route) { inclusive = true }
                    }
                },

                // Add a callback for what happens if the user presses back on the unlock screen
                onNavigateBack = {
                    navController.popBackStack(Screen.OnboardingScreen.route, false)
                }

            )
        }

        composable(Screen.AutoUnlockScreen.route) {
            AutoUnlockScreen(
                onUnlocked = {
                    navController.navigate(Screen.HomeDashboardScreen.route) {
                        popUpTo(Screen.AutoUnlockScreen.route) { inclusive = true }
                    }
                },
                onNavigateToPasswordUnlock = {
                    navController.navigate(Screen.UnlockScreen.route)
                }
            )
        }

        // The main part of your application after authentication
        composable(Screen.HomeDashboardScreen.route) {
            HomeDashboardScreen(appNavController = navController, vaultLockManager = vaultLockManager)
        }

        composable(
            route = Screen.AddPasswordScreen.route,
            arguments = listOf(
                navArgument("passwordId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddPasswordScreen(
                onClose = { navController.popBackStack() },
                vaultLockManager = vaultLockManager
                // `onSave` logic is handled inside its ViewModel
            )
        }

        composable(
            route = Screen.DetailPasswordScreen.route,
            arguments = listOf(navArgument("passwordId") { type = NavType.IntType })
        ) {
            // Your PasswordDetailScreen will get the id from the SavedStateHandle in its ViewModel
            PasswordDetailScreen(
                onBack = {navController.popBackStack()},
                onEdit = {id ->
                    navController.navigate(Screen.AddPasswordScreen.createRoute(id))
                },
                onDeleted = { navController.popBackStack() }
            )
        }

        composable(Screen.ChangeMasterPasswordScreen.route) {
            ChangeMasterPasswordScreen(
                onBack = {navController.popBackStack()},
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}


@Composable
fun DashboardNavGraph(
    dashboardNavController: NavHostController,
    appNavController: NavHostController,
    onNavigateToUnlock: () -> Unit,
    onNavigateToAddPassword: () -> Unit,
    vaultLockManager: VaultLockManager
) {
    NavHost(dashboardNavController, startDestination = BottomNavRoute.Home.route) {
        composable(BottomNavRoute.Home.route) {
            VaultScreen(
                appNavController = appNavController,
                onNavigateToAddPasswordScreen = onNavigateToAddPassword
            )
        }

        composable(BottomNavRoute.Generator.route) {
            PasswordGeneratorScreen()
        }
        composable(BottomNavRoute.Settings.route) {
            VaultSettingScreen(
                onChangeMasterPassword = {
                    appNavController.navigate(Screen.ChangeMasterPasswordScreen.route)
                },
                onBackupRestoreClick = {},
                onAboutClick = {},
                vaultLockManager = vaultLockManager
            )
        }
    }
}
