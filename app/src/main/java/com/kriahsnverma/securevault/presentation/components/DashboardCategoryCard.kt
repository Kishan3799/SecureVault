package com.kriahsnverma.securevault.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriahsnverma.securevault.domain.model.CategoryItem
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme


@Composable
fun DashboardCategoryCard(item: CategoryItem) {
    Column(
        modifier = Modifier.run {
            width(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSecondary)
                .padding(vertical = 12.dp)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.name,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${item.count}",
            color = MaterialTheme.colorScheme.outline,
            fontSize = 10.sp
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SecureVaultTheme {
         DashboardCategoryCard(CategoryItem("Dinner", icon = Icons.Default.Air, count = 1))
    }
}