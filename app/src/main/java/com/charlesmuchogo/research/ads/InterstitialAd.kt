package com.charlesmuchogo.research.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.HOMEPAGE_AD_UNIT_ID
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private var mInterstitialAd: InterstitialAd? = null

// Suspend until ad is loaded or fails
private suspend fun loadInterstitialAd(context: Context, addUnit: String): InterstitialAd? =
    suspendCancellableCoroutine { continuation ->
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, addUnit, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("Ad", "✅ Interstitial ad loaded successfully.")
                mInterstitialAd = interstitialAd
                continuation.resume(interstitialAd)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.e("Ad", "❌ Failed to load interstitial ad: ${loadAdError.message}")
                mInterstitialAd = null
                continuation.resume(null)
            }
        })
    }

suspend fun showInterstitialAd(context: Context, onShowAd: () -> Unit = {}, addUnit: String = HOMEPAGE_AD_UNIT_ID ) {
    val interstitialAd = mInterstitialAd ?: loadInterstitialAd(context, addUnit)

    if (interstitialAd == null) {
        Log.d("Ad", "⚠️ Interstitial ad not ready to show.")
        return
    }

    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            Log.d("Ad", "Interstitial ad dismissed.")
            mInterstitialAd = null
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            Log.e("Ad", "Failed to show interstitial ad: ${p0.message}")
            mInterstitialAd = null
        }

        override fun onAdShowedFullScreenContent() {
            Log.d("Ad", "Interstitial ad is now showing.")
            onShowAd()
        }
    }

    interstitialAd.show(context as Activity)
}
