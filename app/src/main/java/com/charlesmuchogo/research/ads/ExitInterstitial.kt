package com.charlesmuchogo.research.ads

import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.utils.findActivity

@Composable
fun rememberExitWithInterstitial(
    placement: InterstitialPlacement,
    minimumVisibleMs: Long = 0L,
    eligible: () -> Boolean = { true },
): () -> Unit {
    val adsManager = rememberAdsManager()
    val context = LocalContext.current
    val openedAt = remember { SystemClock.elapsedRealtime() }
    val isEligible = rememberUpdatedState(eligible)

    val exit: () -> Unit = {
        val visibleFor = SystemClock.elapsedRealtime() - openedAt
        val activity = context.findActivity()
        if (activity != null && visibleFor >= minimumVisibleMs && isEligible.value()) {
            adsManager.showInterstitial(activity, placement)
        }
        navController.popBackStack()
    }

    BackHandler(onBack = exit)
    return exit
}
