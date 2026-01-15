package com.kriahsnverma.securevault.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavRoute(val route:String, val label:String, val icon: ImageVector){
    object Home : BottomNavRoute("vault_screen", "Home", Icons.Outlined.Home)
    object Generator : BottomNavRoute("password_generator_screen", "Generator", Icons.Outlined.Key)
    object Settings : BottomNavRoute("vault_setting_screen", "Settings", Icons.Outlined.Settings)
}