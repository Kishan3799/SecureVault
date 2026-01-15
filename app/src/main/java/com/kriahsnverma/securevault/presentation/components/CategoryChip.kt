package com.kriahsnverma.securevault.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(
    // 1. Rename 'text' to 'category'
    category: String,
    // 2. Rename 'selected' to 'isSelected'
    isSelected: Boolean,
    // 3. Rename 'onClick' to 'onSelect'
    onSelect: () -> Unit
) {
    Surface(
        // Use the 'onSelect' parameter for the click action
        onClick = onSelect,
        shape = RoundedCornerShape(100), // Using 100 or 50.dp for a pill shape
        // Use the 'isSelected' parameter to determine the color
        color = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            // Use the 'category' parameter for the text
            text = category,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            // Set text color based on the background for better contrast
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}