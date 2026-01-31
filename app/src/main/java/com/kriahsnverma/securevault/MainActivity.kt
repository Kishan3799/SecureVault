package com.kriahsnverma.securevault

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.core.util.InactivityManager
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.presentation.navigation.RootNavGraph
import com.kriahsnverma.securevault.presentation.viewmodel.VaultSettingViewModel
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var vaultLockManager: VaultLockManager
    private lateinit var inactivityManager: InactivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inactivityManager = InactivityManager {
            vaultLockManager.lock() // This triggers MasterKeyHolder.clear()
        }
//        enableEdgeToEdge()
        setContent {
            val viewModel : VaultSettingViewModel= hiltViewModel()
            val currentTheme by viewModel.appTheme.collectAsState()
            val darkTheme = when(currentTheme) {
                "Light" -> false
                "Dark" -> true
                else -> isSystemInDarkTheme()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            inactivityManager.resetTimer()
                        })
                    }
            ) {
                SecureVaultTheme(darkTheme = darkTheme) {
                    RootNavGraph(vaultLockManager = vaultLockManager)
                }
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        inactivityManager.resetTimer()
    }
}
