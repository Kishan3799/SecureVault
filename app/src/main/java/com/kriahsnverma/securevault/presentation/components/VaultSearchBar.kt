package com.kriahsnverma.securevault.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VaultSearchBar(
    // 1. Rename 'value' to 'query' to match the usage in VaultScreen
    query: String,
    // 2. Rename 'onValueChange' to 'onQueryChange' for consistency
    onQueryChange: (String) -> Unit,
    // 3. Add the Modifier parameter
    modifier: Modifier = Modifier // Default modifier is good practice
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        // 4. Apply the passed-in modifier to the TextField
        modifier = modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search Icon") },
        placeholder = { Text("Search in Vault") },
        shape = RoundedCornerShape(16.dp), // A slightly larger radius often looks better
        colors = TextFieldDefaults.colors(
            // Use colors that adapt to the theme better
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            // Hide the indicator line for a cleaner, modern search bar look
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
            disabledIndicatorColor = MaterialTheme.colorScheme.surface,
        ),
        singleLine = true // Ensures the search bar doesn't expand into multiple lines
    )
}
