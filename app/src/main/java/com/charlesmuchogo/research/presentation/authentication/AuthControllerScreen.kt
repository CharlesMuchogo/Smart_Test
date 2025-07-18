package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.utils.ResultStatus


@Composable
fun AuthControllerScreen(
    modifier: Modifier = Modifier,
    screen: @Composable() () -> Unit,
    authRequired: Boolean = true,
) {

    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileStatus by authenticationViewModel.profileStatus.collectAsStateWithLifecycle()

    if (authRequired) {
        when (profileStatus.status) {
            ResultStatus.INITIAL,
            ResultStatus.LOADING -> {
            }

            ResultStatus.ERROR -> {
                screen.invoke()
            }

            ResultStatus.SUCCESS -> {
                if (profileStatus.data != null) {
                    if (profileStatus.data!!.age.isBlank() || profileStatus.data!!.educationLevel.isBlank()) {
                        MoreDetailsScreen()
                    } else {
                        screen.invoke()
                    }
                } else {
                    AuthenticationBlockerScreen(modifier = modifier)
                }
            }
        }
    } else {
        screen.invoke()
    }
}
