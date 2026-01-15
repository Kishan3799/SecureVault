package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.kriahsnverma.securevault.presentation.components.CategoryChip
import com.kriahsnverma.securevault.presentation.components.PasswordCard
import com.kriahsnverma.securevault.presentation.components.SecureVaultBottomNav
import com.kriahsnverma.securevault.presentation.components.VaultSearchBar
import com.kriahsnverma.securevault.presentation.navigation.BottomNavRoute
import com.kriahsnverma.securevault.presentation.navigation.Screen
import com.kriahsnverma.securevault.presentation.viewmodel.VaultViewModel

@Composable
fun VaultScreen(
    appNavController: NavController,
    onNavigateToAddPasswordScreen: () -> Unit = {},
    viewModel: VaultViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = listOf("All", "Login", "Card", "Wi-Fi", "Notes")

    Scaffold(
        topBar = {
            VaultSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchChange,
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddPasswordScreen,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add a new password")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = uiState.selectedCategory == category,
                        onSelect = { viewModel.onCategorySelect(category) }
                    )
                }
            }

            if (uiState.passwords.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (uiState.searchQuery.isNotEmpty() || uiState.selectedCategory != "All") {
                            "No Results Found"
                        } else {
                            "Your Vault is Empty"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (uiState.searchQuery.isNotEmpty() || uiState.selectedCategory != "All") {
                            "Try adjusting your search or filter."
                        } else {
                            "Tap the '+' button to add your first password."
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Apply the key for better performance and stability
                    items(
                        items = uiState.passwords,
                        key = { it.id }
                    ) { password ->
                        PasswordCard(
                            password = password,
                            onClick = {
                                appNavController.navigate(Screen.DetailPasswordScreen.createRoute(password.id))
                            }
                        )
                    }
                }
            }
        }
    }
}