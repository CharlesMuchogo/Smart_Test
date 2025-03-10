package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@Composable
fun AuthControllerScreen(modifier: Modifier = Modifier, navController: NavController) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileStatus =   authenticationViewModel.profileStatus.collectAsStateWithLifecycle().value

    when (profileStatus.status) {
        ResultStatus.INITIAL,
        ResultStatus.LOADING -> { }
        ResultStatus.ERROR -> {
            LoginScreen(navController = navController)
        }

        ResultStatus.SUCCESS -> {
            if (profileStatus.data != null) {
                if (profileStatus.data.age.isBlank() || profileStatus.data.educationLevel.isBlank()) {
                    MoreDetailsScreen(navController = navController)
                } else {
                    HomeScreen(navController = navController)
                }
            } else {
                LoginScreen(navController = navController)
            }
        }
    }
}