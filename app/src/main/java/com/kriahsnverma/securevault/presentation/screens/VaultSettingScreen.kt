package com.kriahsnverma.securevault.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.presentation.components.SectionTitle
import com.kriahsnverma.securevault.presentation.components.SettingsCard
import com.kriahsnverma.securevault.presentation.components.SettingsItem
import com.kriahsnverma.securevault.presentation.components.SettingsSwitchItem
import com.kriahsnverma.securevault.presentation.util.isBiometricAvailable
import com.kriahsnverma.securevault.presentation.viewmodel.VaultSettingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultSettingScreen(
    onChangeMasterPassword: () -> Unit,
    onBackupRestoreClick: () -> Unit,
    onAboutClick: () -> Unit,
    viewModel: VaultSettingViewModel = hiltViewModel(),
    vaultLockManager: VaultLockManager
) {
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val context = LocalContext.current
    val currentTheme by viewModel.appTheme.collectAsState()
    val currentAutoLockMinutes by viewModel.autoLockMinutes.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showAutoLockDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Select Theme") },
            text = {
                Column {
                    val themes = listOf("Light", "Dark", "System")
                    themes.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (theme == currentTheme),
                                    onClick = {
                                        viewModel.setTheme(theme)
                                        showThemeDialog = false
                                    }
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (theme == currentTheme),
                                onClick = null
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(text = theme)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAutoLockDialog) {
        AlertDialog(
            onDismissRequest = { showAutoLockDialog = false },
            title = { Text("Auto-Lock Timeout") },
            text = {
                Column {
                    val options = listOf(
                        1 to "1 Minute",
                        5 to "5 Minutes",
                        10 to "10 Minutes",
                        30 to "30 Minutes",
                        -1 to "Never"
                    )
                    options.forEach { (minutes, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (minutes == currentAutoLockMinutes),
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.setAutoLockMinutes(minutes)
                                            vaultLockManager.updateAutoLockMinutes(minutes)
                                            showAutoLockDialog = false
                                        }
                                    }
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (minutes == currentAutoLockMinutes),
                                onClick = null
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(text = label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAutoLockDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SectionTitle("Security")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.VpnKey,
                        title = "Change Master Password",
                        onClick = {
                            onChangeMasterPassword()
                        }
                    )

                    HorizontalDivider()

                    SettingsSwitchItem(
                        icon = Icons.Default.Fingerprint,
                        title = "Biometric Login",
                        subtitle = "Use fingerprint or face to unlock",
                        checked = biometricEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled && !isBiometricAvailable(context)) {
                                Toast.makeText(context, "Biometric authentication not available on this device", Toast.LENGTH_SHORT).show()
                                return@SettingsSwitchItem
                            }
                            viewModel.setBiometricEnabled(enabled)
                        }
                    )

                    HorizontalDivider()

                    SettingsItem(
                        icon = Icons.Default.Timer,
                        title = "Auto-Lock Timeout",
                        subtitle = when (currentAutoLockMinutes) {
                            -1 -> "Never"
                            1 -> "1 Minute"
                            else -> "$currentAutoLockMinutes Minutes"
                        },
                        onClick = { showAutoLockDialog = true }
                    )
                }
            }

            item {
                SectionTitle("Appearance")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Palette,
                        title = "Theme",
                        subtitle = currentTheme,
                        onClick = { showThemeDialog = true }
                    )
                }
            }

            item {
                SectionTitle("Data")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Backup,
                        title = "Backup & Restore",
                        subtitle = "Encrypted File",
                        onClick = onBackupRestoreClick
                    )
                }
            }

            item {
                SectionTitle("About")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "About & Help",
                        onClick = onAboutClick
                    )
                }
            }
        }
    }
}
