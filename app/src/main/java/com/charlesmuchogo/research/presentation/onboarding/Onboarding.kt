package com.charlesmuchogo.research.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.navigation.LoginPage
import com.charlesmuchogo.research.presentation.onboarding.components.OnboardingContent
import kotlinx.coroutines.delay

@Composable
fun OnboardingRoot() {

    val viewModel = hiltViewModel<OnboardingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit = {},
) {

    val pagerState = rememberPagerState(pageCount = {
        3
    })

    LaunchedEffect(state.currentSlide) {
        if (state.currentSlide < pagerState.pageCount) {
            delay(5000)
            onAction.invoke(OnboardingAction.OnChangeSlide(slide = state.currentSlide + 1))
        }
    }

    LaunchedEffect(pagerState.currentPage){
        onAction.invoke(OnboardingAction.OnChangeSlide(slide = pagerState.currentPage))
    }

    LaunchedEffect(state.currentSlide){
        pagerState.animateScrollToPage(state.currentSlide)
    }


        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        OnboardingContent(
                            title = "Learn more about HIV/ADS",
                            text = "Learn more about HIV/AIDS, how it spreads, and discover effective ways to protect yourself and stay healthy.",
                            painter = painterResource(R.drawable.learnmore)
                        )
                    }

                    1 -> {
                        OnboardingContent(
                            title = "Take a test",
                            text = "Discover how to protect yourself from HIV/AIDS, take a quick test, and get instant, confidential results.",
                            painter = painterResource(R.drawable.labresults)
                        )
                    }

                    2 -> {
                        OnboardingContent(
                            title = "Talk to a physician",
                            text = "Get connected to a nearby care facility for support, treatment, and guidance tailored to your HIV/AIDS needs.",
                            painter = painterResource(R.drawable.connections)
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0..2) {
                        Spacer(
                            Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (pagerState.currentPage != i) MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.2f
                                    ) else MaterialTheme.colorScheme.primary
                                )
                        )
                    }
                }

                TextButton(
                    onClick = {
                        onAction(OnboardingAction.SaveFirstTime(true))
                        navController.navigate(LoginPage) },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 80.dp)
                ) {
                    Text(text = if(pagerState.currentPage == 2) "Get Started" else "Skip" , style = MaterialTheme.typography.bodyMedium)
                }


            }

    }
}