package com.kriahsnverma.securevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.kriahsnverma.securevault.core.util.InactivityManager
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.presentation.navigation.RootNavGraph
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var vaultLockManager: VaultLockManager
    private lateinit var inactivityManager: InactivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inactivityManager = InactivityManager {
            vaultLockManager.lock() // This triggers MasterKeyHolder.clear()
        }
        enableEdgeToEdge()
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            inactivityManager.resetTimer()
                        })
                    }
            )
            SecureVaultTheme {
                RootNavGraph()
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        inactivityManager.resetTimer()
    }
}