package com.kriahsnverma.securevault.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kriahsnverma.securevault.presentation.components.InfoCard
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Help & Support",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                title = "How is my data secured?",
                description = "All passwords and notes are encrypted using AES-256-GCM. The encryption key is derived from your Master Password using PBKDF2. We never store your Master Password."
            )

            InfoCard(
                title = "What if I forget my Master Password?",
                description = "Due to the zero-knowledge security model, if you lose your Master Password, your data cannot be recovered. There is no 'Reset Password' feature."
            )

            InfoCard(
                title = "Biometric Unlock",
                description = "Biometric unlock stores an encrypted version of your Master Password in the Android Keystore. You can enable or disable this in Settings."
            )

            InfoCard(
                title = "Backup & Restore",
                description = "You can export your vault as an encrypted JSON file. Keep this file safe. To restore, you will need the Master Password that was active when the backup was created."
            )

            InfoCard(
                title = "Need more help?",
                description = "If you encounter any issues or have suggestions, feel free to reach out to the developer via GitHub or email."
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HelpScreenPreview() {
    SecureVaultTheme {
        HelpScreen(onBack = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HelpScreenPreviewDark() {
    SecureVaultTheme {
        HelpScreen(onBack = {})
    }
}
