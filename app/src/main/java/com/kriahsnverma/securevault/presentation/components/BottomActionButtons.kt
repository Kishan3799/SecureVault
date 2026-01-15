package com.kriahsnverma.securevault.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomActionButtons(
    onGenerate: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedButton(
            onClick = onGenerate,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text("Generate Password")
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text("Save")
        }
    }
}
