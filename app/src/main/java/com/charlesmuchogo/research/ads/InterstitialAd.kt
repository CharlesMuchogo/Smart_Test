package com.charlesmuchogo.research.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.HOMEPAGE_AD_UNIT_ID
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

private const val INTERSTITIAL_COOLDOWN_MS = 3 * 60 * 1000L

private val loadedAds = mutableMapOf<String, InterstitialAd>()
private val showMutex = Mutex()
private var lastShownAtMs = 0L

// Suspend until ad is loaded or fails
private suspend fun loadInterstitialAd(context: Context, addUnit: String): InterstitialAd? =
    suspendCancellableCoroutine { continuation ->
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, addUnit, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("Ad", "✅ Interstitial ad loaded successfully.")
                loadedAds[addUnit] = interstitialAd
                continuation.resume(interstitialAd)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.e("Ad", "❌ Failed to load interstitial ad: ${loadAdError.message}")
                loadedAds.remove(addUnit)
                continuation.resume(null)
            }
        })
    }

private suspend fun isSubscribed(context: Context): Boolean {
    val preferenceManager = PreferenceManager(
        SharedPreferencesSettings(
            context.applicationContext.getSharedPreferences(
                PreferenceManager.DATASTORE_FILE_NAME,
                Context.MODE_PRIVATE,
            )
        )
    )
    return preferenceManager.getBoolean(PreferenceManager.SUBSCRIPTION_ACTIVE).first()
}

suspend fun showInterstitialAd(context: Context, onShowAd: () -> Unit = {}, addUnit: String = HOMEPAGE_AD_UNIT_ID) {
    if (isSubscribed(context)) {
        Log.d("Ad", "Skipping interstitial ad — user is subscribed.")
        return
    }

    showMutex.withLock {
        val now = System.currentTimeMillis()
        if (now - lastShownAtMs < INTERSTITIAL_COOLDOWN_MS) {
            Log.d("Ad", "Skipping interstitial ad — still within cooldown window.")
            return
        }

        val interstitialAd = loadedAds[addUnit] ?: loadInterstitialAd(context, addUnit)

        if (interstitialAd == null) {
            Log.d("Ad", "⚠️ Interstitial ad not ready to show.")
            return
        }

        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("Ad", "Interstitial ad dismissed.")
                loadedAds.remove(addUnit)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.e("Ad", "Failed to show interstitial ad: ${p0.message}")
                loadedAds.remove(addUnit)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("Ad", "Interstitial ad is now showing.")
                lastShownAtMs = System.currentTimeMillis()
                onShowAd()
            }
        }

        interstitialAd.show(context as Activity)
    }
}
