package com.kriahsnverma.securevault.presentation.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriahsnverma.securevault.R
import com.kriahsnverma.securevault.domain.model.OnboardingPageItem
import com.kriahsnverma.securevault.presentation.components.OnboardingPageContent
import com.kriahsnverma.securevault.ui.theme.SecureVaultTheme
import kotlinx.coroutines.launch

val onboardingPage = listOf(
    OnboardingPageItem(
        title = "Secure Vault",
        description = "Store all your passwords securely with industry-standard AES-256 encryption.",
        image = R.drawable.onboarding1
    ),
    OnboardingPageItem(
        title = "Password Generator",
        description = "Instantly create complex and unique passwords for every account to stay safe.",
        image = R.drawable.onboarding2
    ),
    OnboardingPageItem(
        title = "Privacy First",
        description = "Your data never leaves your device. We follow a zero-knowledge architecture.",
        image = R.drawable.onbaording3
    )
)

@Composable
fun OnboardingScreen(
    onNavigateToSetup: () -> Unit
) {
    val pageState = rememberPagerState(pageCount = { onboardingPage.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage by remember { derivedStateOf { pageState.currentPage == onboardingPage.size - 1 } }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Title
            Text(
                text = "SecureVault",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 48.dp, bottom = 32.dp)
            )

            HorizontalPager(
                state = pageState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                OnboardingPageContent(page = onboardingPage[pageIndex])
            }

            // Page Indicator Dots
            PageIndicator(pagerState = pageState)

            Spacer(modifier = Modifier.height(40.dp))

            // Action Button
            Button(
                onClick = {
                    if (isLastPage) {
                        onNavigateToSetup()
                    } else {
                        coroutineScope.launch {
                            pageState.animateScrollToPage(
                                page = pageState.currentPage + 1,
                                animationSpec = tween(500)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = if (isLastPage) "Get Started" else "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PageIndicator(pagerState: PagerState) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(pagerState.pageCount) { index ->
            val isSelected = index == pagerState.currentPage
            val width by animateDpAsState(
                targetValue = if (isSelected) 32.dp else 8.dp,
                animationSpec = tween(300),
                label = "indicator_width"
            )
            val color = if (isSelected) 
                MaterialTheme.colorScheme.onPrimary 
            else 
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
            
            if (index < pagerState.pageCount - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun OnBoardingPreview() {
    SecureVaultTheme {
        OnboardingScreen(onNavigateToSetup = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingPreviewDark() {
    SecureVaultTheme {
        OnboardingScreen(onNavigateToSetup = {

        })
    }
}
