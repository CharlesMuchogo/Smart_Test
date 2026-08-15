package com.charlesmuchogo.research.presentation.subscription

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.domain.viewmodels.SubscriptionUpsellViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.SubscribePage
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@Composable
fun SubscriptionUpsellController() {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileStatus by authenticationViewModel.profileStatus.collectAsStateWithLifecycle()

    val upsellViewModel = hiltViewModel<SubscriptionUpsellViewModel>()
    val isSubscribed by upsellViewModel.isSubscribed.collectAsStateWithLifecycle()
    val upsellShown by upsellViewModel.upsellShown.collectAsStateWithLifecycle()

    val user = profileStatus.data
    val isProfileComplete = user != null && user.age.isNotBlank() && user.educationLevel.isNotBlank()
    val shouldShow = profileStatus.status == ResultStatus.SUCCESS &&
        isProfileComplete &&
        !isSubscribed &&
        !upsellShown

    if (shouldShow) {
        SubscriptionUpsellBottomSheet(
            onDismiss = { upsellViewModel.onUpsellDismissed() },
            onSubscribeClick = {
                upsellViewModel.onUpsellDismissed()
                navController.navigate(SubscribePage)
            },
        )
    }
}
