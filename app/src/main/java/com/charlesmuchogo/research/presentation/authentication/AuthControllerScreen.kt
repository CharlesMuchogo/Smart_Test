package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.authentication.login.LoginRoot
import com.charlesmuchogo.research.presentation.authentication.login.LoginScreen
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
import com.charlesmuchogo.research.presentation.onboarding.OnboardingRoot
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@Composable
fun AuthControllerScreen(modifier: Modifier = Modifier, navController: NavController) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileStatus by authenticationViewModel.profileStatus.collectAsStateWithLifecycle()

    val isFirstTime by authenticationViewModel.isFirstTime.collectAsStateWithLifecycle()

    when(isFirstTime){
        false -> {
            OnboardingRoot()
        }
        true -> {
            when (profileStatus.status) {
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> { }
                ResultStatus.ERROR -> {
                    LoginRoot()
                }

                ResultStatus.SUCCESS -> {
                    if (profileStatus.data != null) {
                        if (profileStatus.data!!.age.isBlank() || profileStatus.data!!.educationLevel.isBlank()) {
                            MoreDetailsScreen(navController = navController)
                        } else {
                            HomeScreen(navController = navController)
                        }
                    } else {
                        LoginRoot()
                    }
                }
            }
        }
        null -> {

        }
    }

}