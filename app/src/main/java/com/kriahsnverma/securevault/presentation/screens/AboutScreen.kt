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
fun AboutScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "About SecureVault",
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
                title = "What is SecureVault?",
                description = "SecureVault is an offline-first password manager designed with a focus on privacy and security. It uses industry-standard AES-256 encryption to keep your sensitive data safe on your device."
            )

            InfoCard(
                title = "Zero-Knowledge Architecture",
                description = "We believe in your privacy. SecureVault follows a zero-knowledge architecture, meaning your master password and data never leave your device and are never stored on any servers."
            )

            InfoCard(
                title = "Key Features",
                description = """
                    • AES-256 Military Grade Encryption
                    • Biometric Unlock (Fingerprint/Face)
                    • Secure Offline Storage
                    • Encrypted JSON Backup & Restore
                    • Built-in Password Generator
                """.trimIndent()
            )

            InfoCard(
                title = "Developer Information",
                description = "Developed by Kishan Verma.\nGitHub: github.com/Kishan3799\n\nVersion: 1.0.0"
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AboutScreenPreview() {
    SecureVaultTheme {
        AboutScreen(onBack = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AboutScreenPreviewDark() {
    SecureVaultTheme {
        AboutScreen(onBack = {})
    }
}
