package com.kriahsnverma.securevault.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriahsnverma.securevault.domain.model.OnboardingPageItem

@Composable
fun OnboardingPageContent(page: OnboardingPageItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 2. Image/Illustration Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Ensure it's a square as shown
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for an icon or a more complex illustration
//            Icon(
//                imageVector = ,
//                contentDescription = "Illustration Placeholder",
//                tint = Color(0xFFAAAAAA),
//                modifier = Modifier.size(64.dp)
//            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Title Text
        Text(
            text = page.title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Subtitle Text
        Text(
            text = page.description,
            color = Color.White.copy(alpha = 0.7f), // Lighter text color
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f) // Slightly narrower width
        )
    }
}