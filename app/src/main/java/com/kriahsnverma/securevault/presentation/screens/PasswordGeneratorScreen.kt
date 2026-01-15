package com.kriahsnverma.securevault.presentation.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.core.util.copyToClipboard
import com.kriahsnverma.securevault.presentation.components.GeneratorOption
import com.kriahsnverma.securevault.presentation.viewmodel.PasswordGeneratorViewModel
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    viewModel: PasswordGeneratorViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Password Generator")
                },
//                navigationIcon = {
//                    IconButton(onClick = { /* Handle back navigation */ }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ){
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text("Password Length")
                        Text(
                            text = state.length.toString(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Slider(
                        value = state.length.toFloat(),
                        onValueChange = { viewModel.onLengthChange(it.toInt()) },
                        valueRange = 6f..32f,
                        steps = 26
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("6")
                        Text("32")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {

                    GeneratorOption(
                        text = "Include Uppercase",
                        checked = state.includeUppercase,
                        onCheckedChange = viewModel::onUppercaseToggle
                    )

                    GeneratorOption(
                        text = "Include Lowercase",
                        checked = state.includeLowercase,
                        onCheckedChange = viewModel::onLowercaseToggle
                    )

                    GeneratorOption(
                        text = "Include Numbers",
                        checked = state.includeNumber,
                        onCheckedChange = viewModel::onNumberToggle
                    )

                    GeneratorOption(
                        text = "Include Symbols",
                        checked = state.includeSymbols,
                        onCheckedChange = viewModel::onSymbolToggle
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.generatedPassword,
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = {
                        copyToClipboard(context, "Password", state.generatedPassword)
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    SecureVaultTheme {
        PasswordGeneratorScreen()
    }
    
}