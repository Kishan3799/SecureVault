package com.kriahsnverma.securevault.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.kriahsnverma.securevault.core.util.copyToClipboard
import com.kriahsnverma.securevault.domain.model.PasswordDetailUiState
import com.kriahsnverma.securevault.presentation.components.DetailCard
import com.kriahsnverma.securevault.presentation.viewmodel.PasswordDetailViewModel
import javax.crypto.SecretKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreen(
    // The screen no longer needs passwordId or masterKey as parameters
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onDeleted: () -> Unit,
    viewModel: PasswordDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    // No LaunchedEffect is needed anymore, the ViewModel handles loading in its init block.

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // 1. Handle the different states from the ViewModel
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when (val state = uiState) {
                is PasswordDetailUiState.Loading -> {
                    // Show a loading indicator
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PasswordDetailUiState.Error -> {
                    // Show an error message
                    Text(
                        text = state.message,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is PasswordDetailUiState.Success -> {
                    // This is your existing UI for the success state
                    PasswordDetailContent(
                        data = state,
                        showPassword = showPassword,
                        onTogglePasswordVisibility = { showPassword = !showPassword },
                        onEdit = { onEdit(state.id) },
                        onDelete = { viewModel.deletePassword(onDeleteComplete = onDeleted) },
                        context = context
                    )
                }

//                is PasswordDetailUiState.Error -> TODO()
//                PasswordDetailUiState.Loading -> TODO()
//                is PasswordDetailUiState.Success -> TODO()
            }
        }
    }
}

// 2. Extract the success UI into its own composable for clarity
@Composable
private fun PasswordDetailContent(
    data: PasswordDetailUiState.Success,
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    context: Context,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                // Icon logic for websites could be improved later
                Icon(
                    Icons.Default.Language,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(data.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                data.url?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        DetailCard(
            label = "Username",
            value = data.username,
            onCopy = { copyToClipboard(context, "Username", data.username) }
        )

        Spacer(Modifier.height(12.dp))

        DetailCard(
            label = "Password",
            value = if (showPassword) data.decryptedPassword else "•".repeat(data.decryptedPassword.length.coerceAtLeast(8)),
            trailing = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, "Toggle password visibility")
                }
            },
            onCopy = { copyToClipboard(context, "Password", data.decryptedPassword) }
        )

        if (!data.notes.isNullOrBlank()) {
            Spacer(Modifier.height(20.dp))
            Text("Notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(data.notes, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.weight(1f))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onEdit) {
                Text("Edit")
            }
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}