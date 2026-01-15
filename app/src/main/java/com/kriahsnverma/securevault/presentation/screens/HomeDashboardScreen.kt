package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kriahsnverma.securevault.domain.model.CategoryItem
import com.kriahsnverma.securevault.domain.model.VaultItem
import com.kriahsnverma.securevault.presentation.components.DashboardBottomBar
import com.kriahsnverma.securevault.presentation.components.DashboardCategoryCard
import com.kriahsnverma.securevault.presentation.components.DashboardSearchBar
import com.kriahsnverma.securevault.presentation.components.DashboardTopHeader
import com.kriahsnverma.securevault.presentation.components.SecureVaultBottomNav
import com.kriahsnverma.securevault.presentation.navigation.DashboardNavGraph
import com.kriahsnverma.securevault.presentation.navigation.Screen


@Composable
fun HomeDashboardScreen(appNavController: NavHostController) {
    val dashboardNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            SecureVaultBottomNav(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    dashboardNavController.navigate(route) {
                        // Logic to avoid multiple copies of the same screen on the back stack
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
        // Host the DashboardNavGraph inside the Scaffold's content area
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
                    appNavController.navigate(Screen.AddPasswordScreen.route)
                }
            )
        }
    }
    /*Scaffold(
        containerColor = MaterialTheme.colorScheme.onSecondary,
        bottomBar = { DashboardBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { *//* Add new item *//* },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            DashboardTopHeader()

            Spacer(modifier = Modifier.height(20.dp))

            DashboardSearchBar()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Categories",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            CategoriesGrid()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recently Used",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))
            RecentItemsList()
        }

    }*/
}

/*@Composable
fun RecentItemsList() {
    val recentItems = listOf(
        VaultItem(id= 1, title="Netflix", subtitle =  "alex@email.com", icon = Icons.Default.Lock),
        VaultItem(id = 2, title ="Spotify", subtitle = "alex.music", icon =  Icons.Default.Lock),
        VaultItem(id = 3, title ="Chase Bank", subtitle =  "4400 **** **** 1234", icon =  Icons.Default.CreditCard),
        VaultItem(id = 4, title="Home Wi-Fi", subtitle =  "Router Admin", icon =  Icons.Default.Wifi),
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        // Use weight or fixed height if nested in a Column without scrolling parent
        modifier = Modifier.fillMaxHeight()
    ) {
        items(recentItems) { item ->
            RecentItemRow(item)
        }
    }
}

@Composable
fun RecentItemRow(item: VaultItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifiera
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.subtitle,
                color = TextSecondaryDark,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = { *//* Copy password logic *//* }) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = "Copy",
                tint = TextSecondaryDark,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CategoriesGrid() {
    val categories = listOf(
        CategoryItem("Logins", Icons.Default.Key, 142),
        CategoryItem("Cards", Icons.Default.CreditCard, 8),
        CategoryItem("Wi-Fi", Icons.Default.Wifi, 4),
        CategoryItem("Notes", Icons.Default.Note, 12)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        categories.forEach { category ->
            DashboardCategoryCard(category)
        }
    }
}*/


