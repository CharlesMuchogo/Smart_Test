package com.charlesmuchogo.research.ads

import com.charlesmuchogo.research.BuildConfig

object AdUnits {
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/9214589741"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARDED = "ca-app-pub-3940256099942544/5224354917"
    private const val BANNER = "ca-app-pub-6323830758154106/9756149016"
    private const val ARTICLES_INTERSTITIAL = "ca-app-pub-6323830758154106/6100639990"
    private const val CHAT_INTERSTITIAL = "ca-app-pub-6323830758154106/8535231644"
    private const val CHAT_REWARDED = "ca-app-pub-6323830758154106/2412142895"

    val banner: String get() = if (BuildConfig.DEBUG) TEST_BANNER else BANNER
    val articlesInterstitial: String get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL else ARTICLES_INTERSTITIAL
    val chatInterstitial: String get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL else CHAT_INTERSTITIAL
    val chatRewarded: String get() = if (BuildConfig.DEBUG) TEST_REWARDED else CHAT_REWARDED
}

enum class InterstitialPlacement {
    ARTICLE_EXIT,
    ASSISTANT_EXIT;

    val unitId: String
        get() = when (this) {
            ARTICLE_EXIT -> AdUnits.articlesInterstitial
            ASSISTANT_EXIT -> AdUnits.chatInterstitial
        }
}
