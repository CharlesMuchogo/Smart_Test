package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.bottomBar.BottomBarRoot
import com.charlesmuchogo.research.presentation.onboarding.OnboardingRoot

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.OnBoardingControllerScreen(navController: NavController, animatedVisibilityScope: AnimatedVisibilityScope) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()

    val isFirstTime by authenticationViewModel.isFirstTime.collectAsStateWithLifecycle()

    when(isFirstTime){
        false -> {
            OnboardingRoot()
        }
        true -> {
            BottomBarRoot(animatedVisibilityScope)
        }
        null -> {

        }
    }

}