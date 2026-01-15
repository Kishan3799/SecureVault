package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
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
import androidx.compose.ui.text.style.TextAlign
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
        description = "Store all your passwords securely",
        image = R.drawable.ic_launcher_foreground
    ),
    OnboardingPageItem(
        title = "Generate ultra-strong passwords",
        description = "Instantly create complex and unique passwords for every account.",
        image = R.drawable.ic_launcher_foreground
    ),
    OnboardingPageItem(
        title = "Sync across all your devices",
        description = "Access your protected data seamlessly on mobile, tablet, and desktop.",
        image = R.drawable.ic_launcher_foreground
    )
)




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onNavigateToSetup: () -> Unit
) {
    val pageState = rememberPagerState(pageCount = { onboardingPage.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage by remember { derivedStateOf { pageState.currentPage == onboardingPage.size - 1 }}

    Scaffold (
        containerColor = MaterialTheme.colorScheme.primary
    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //1. Header Title
            Text(
                text = "Password Manager",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 40.dp, bottom = 32.dp)
            )

            HorizontalPager(
                state = pageState,
                modifier = Modifier.weight(1f),
            ) { pageIndex ->
                OnboardingPageContent(page = onboardingPage[pageIndex])
            }

            // 5. Page Indicator Dots
            PageIndicator(pagerState = pageState)


            Spacer(modifier = Modifier.height(16.dp))

            // 6. Action Button
            Button(
                onClick = {
                    if (isLastPage){
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
                    .height(80.dp)
                    .padding(bottom = 24.dp), // Padding from the bottom edge
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary // More theme-aware
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isLastPage) "Get Started" else "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PageIndicator(pagerState: PagerState) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            // Determine color and size based on if it's the current page
            val color = if (index == pagerState.currentPage)  MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            val size = 8.dp

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
            // Add spacing between the dots
            if (index < pagerState.pageCount - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun OnBoardingPreview() {
    SecureVaultTheme {
        OnboardingScreen(onNavigateToSetup = {})
    }
}