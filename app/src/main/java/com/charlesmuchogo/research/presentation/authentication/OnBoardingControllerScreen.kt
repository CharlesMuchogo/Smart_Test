package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.authentication.login.LoginRoot
import com.charlesmuchogo.research.presentation.bottomBar.BottomBarRoot
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
import com.charlesmuchogo.research.presentation.onboarding.OnboardingRoot
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@Composable
fun OnBoardingControllerScreen(modifier: Modifier = Modifier, navController: NavController) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()

    val isFirstTime by authenticationViewModel.isFirstTime.collectAsStateWithLifecycle()

    when(isFirstTime){
        false -> {
            OnboardingRoot()
        }
        true -> {
            BottomBarRoot()
        }
        null -> {

        }
    }

}