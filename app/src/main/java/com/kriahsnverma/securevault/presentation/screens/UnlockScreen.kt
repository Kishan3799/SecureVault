package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.presentation.viewmodel.UnlockViewModel
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme

@Composable
fun UnlockScreen(
    onUnlocked: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: UnlockViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.unlockedEvent.collect {
            onUnlocked()
        }
    }

    val uiState = viewModel.uiState

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)){
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null, // Decorative icon
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
                .alpha(0.1f), // Reduced alpha for subtlety
            // Use a color that works on the background
            tint = MaterialTheme.colorScheme.primary
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                "Unlock Vault",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Master Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    // Disable field while loading
                    enabled = !uiState.isLoading,
                    isError = uiState.error != null
                )

                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(24.dp))

                if(uiState.isLoading){
                    CircularProgressIndicator()
                }else{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { viewModel.onUnlockClicked() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            // Disable button when loading is handled by the parent Column
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Unlock")
                        }

                        Spacer(Modifier.height(16.dp))

                        // Biometric Button
                        Button(
                            onClick = { /* TODO: Implement Biometric Logic */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Use Biometric")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UnlockScreenPreview() {
    SecureVaultTheme {
        UnlockScreen(onUnlocked = {}, onNavigateBack = {})
    }
}





