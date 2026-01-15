@file:OptIn(ExperimentalMaterial3Api::class)

package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.presentation.components.AppTextField
import com.kriahsnverma.securevault.presentation.components.BottomActionButtons
import com.kriahsnverma.securevault.presentation.components.InputLabel
import com.kriahsnverma.securevault.presentation.viewmodel.AddPasswordViewModel
import kotlinx.coroutines.launch


@Composable
fun AddPasswordScreen(
    onClose: () -> Unit = {},
    viewModel: AddPasswordViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var passwordVisible by remember {mutableStateOf(false)}
    var categoryExpanded by remember { mutableStateOf(false) }

    val isEditMode = viewModel.isEditMode

    val categories = listOf("Login", "Card", "Wi-Fi", "Notes")

    if(sheetState.isVisible){
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
            },
            sheetState = sheetState
        ) {
            PasswordGeneratorScreen()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isEditMode) "Edit Password" else "Add Password")  },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            BottomActionButtons(
                onGenerate = {
                    coroutineScope.launch { sheetState.show() }
                },
                onSave = {
                    viewModel.savePassword(onSaveComplete = onClose)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            InputLabel("Title (App/Website name)")
            AppTextField(
                value = viewModel.title,
                onValueChange = { viewModel.onTitleChange(it) },
                placeholder = "e.g., Online Banking"
            )


            InputLabel("Username / Email")
            AppTextField(
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                placeholder = "e.g., your.email@example.com"
            )

            InputLabel("Password")
            AppTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = "Enter your password",
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = "Toggle password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation()
            )

            InputLabel("Notes")
            AppTextField(
                value = viewModel.notes,
                onValueChange = { viewModel.onNotesChange(it) },
                placeholder = "Additional information",
                minLines = 4
            )

            InputLabel("URL")
            AppTextField(
                value = viewModel.url,
                onValueChange = { viewModel.onUrlChange(it) },
                placeholder = "e.g., www.example.com"
            )

            InputLabel("Category")
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                AppTextField(
                    modifier = Modifier.menuAnchor(),
                    value = viewModel.category,
                    onValueChange = {},
                    placeholder = "Select a category",
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown, null)
                    }
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { categoryName ->
                        DropdownMenuItem(
                            text = { Text(categoryName) },
                            onClick = {
                                viewModel.onCategoryChange(categoryName)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

