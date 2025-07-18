package com.charlesmuchogo.research.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.INTERESTIAL_AD_UNIT_ID
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private var mInterstitialAd: InterstitialAd? = null

fun loadInterstitialAd(context: Context) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(context, INTERESTIAL_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            mInterstitialAd = interstitialAd
        }
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            Log.d("Ad displayed", " mInterstitialAd failed to load ${loadAdError.message}")
            mInterstitialAd = null
        }
    })
}

fun showInterstitialAd(context: Context, onShowAd: () -> Unit = {}) {
    if (mInterstitialAd != null) {
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
                loadInterstitialAd(context)
            }
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mInterstitialAd = null
            }
            override fun onAdShowedFullScreenContent() {
                onShowAd()
            }
        }
        mInterstitialAd?.show(context as Activity)
    }
}