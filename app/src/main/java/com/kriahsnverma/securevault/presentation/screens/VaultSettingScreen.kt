package com.kriahsnverma.securevault.presentation.screens

import android.R.attr.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.kriahsnverma.securevault.presentation.components.SectionTitle
import com.kriahsnverma.securevault.presentation.components.SettingsCard
import com.kriahsnverma.securevault.presentation.components.SettingsItem
import com.kriahsnverma.securevault.presentation.components.SettingsSwitchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultSettingScreen(
    onBack: () -> Unit,
    onChangeMasterPassword: ()-> Unit,
    onAutoLockClick: () -> Unit,
    onThemeClick: () -> Unit,
    onBackupRestoreClick: () -> Unit,
    onAboutClick: () -> Unit,

) {
    var biometricEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            item {
                SectionTitle("Security")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.VpnKey,
                        title = "Change Master Password",
                        onClick = onChangeMasterPassword
                    )

                    HorizontalDivider()

                    SettingsSwitchItem(
                        icon = Icons.Default.Fingerprint,
                        title = "Biometric Login",
                        subtitle = "Use fingerprint or face to unlock",
                        checked = biometricEnabled,
                        onCheckedChange = {
                            biometricEnabled = it
                        }
                    )

                    HorizontalDivider()

                    SettingsItem(
                        icon = Icons.Default.Timer,
                        title = "Auto-Lock Timeout",
                        subtitle = "1 min",
                        onClick = onAutoLockClick
                    )
                }
            }

            item {
                SectionTitle("Appereance")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Palette,
                        title = "Theme",
                        subtitle = "Auto",
                        onClick = onThemeClick
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