package com.kriahsnverma.securevault.presentation.screens

import android.text.method.PasswordTransformationMethod
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.presentation.components.CustomPasswordField
import com.kriahsnverma.securevault.presentation.viewmodel.SetupMasterPasswordViewModel



@Composable
fun SetupMasterPasswordScreen(
    // 1. Simplified navigation callbacks
    onSetupComplete: () -> Unit,
    onNavigateBack: () -> Unit,
    // Use the alias 'viewModel' for convention
    viewModel: SetupMasterPasswordViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.setupCompleteEvent.collect {
            onSetupComplete()
        }
    }

    // 3. Get the UI state directly from the ViewModel
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use background, not onSecondary
            .padding(24.dp),
    ) {
        IconButton(
            onClick = onNavigateBack, // Use the callback
            modifier = Modifier.offset(x = (-12).dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Set your Master Password",
            color = MaterialTheme.colorScheme.onBackground, // Use onBackground
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "This password protects your vault. If you lose it, it cannot be recovered.",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 4. Connect UI components directly to the ViewModel's state and events
        CustomPasswordField(
            value = viewModel.password, // Read from ViewModel
            onValueChange = { viewModel.onPasswordChange(it) }, // Notify ViewModel
            placeholder = "Password"
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomPasswordField(
            value = viewModel.confirmPassword, // Read from ViewModel
            onValueChange = { viewModel.onConfirmPasswordChange(it) }, // Notify ViewModel
            placeholder = "Confirm Password"
        )

        Spacer(modifier = Modifier.height(24.dp))

        val strength = uiState.passwordStrength
        val animateColor by animateColorAsState(
            targetValue = strength.color,
            animationSpec = tween(500),
            label = "strengthColor"
        )

        // Password Strength Indicator (This part was already good)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Password Strength", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = strength.displayText,
                color = animateColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { strength.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = animateColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round,
        )

        Spacer(modifier = Modifier.weight(1f))

        // Display the error from the ViewModel's state
        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Show a loading indicator if the process is running
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Button(
            // 5. The button ONLY notifies the ViewModel of the user's intent.
            onClick = { viewModel.saveMasterPasswordAndCompleteSetup() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            // Disable the button while loading to prevent multiple clicks
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Create Vault",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

