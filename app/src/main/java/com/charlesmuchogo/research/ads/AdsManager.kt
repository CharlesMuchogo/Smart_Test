package com.charlesmuchogo.research.ads

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsManager @Inject constructor(
    @ApplicationContext private val context: Context,
    settingsRepository: MultiplatformSettingsRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)
    private val sdkStarted = AtomicBoolean(false)
    private val consentRequested = AtomicBoolean(false)

    private val sessionStartedAt = SystemClock.elapsedRealtime()
    private var lastFullScreenAt = 0L
    private var interstitialsThisSession = 0

    private val interstitials = mutableMapOf<String, CachedAd<InterstitialAd>>()
    private val loadingInterstitials = mutableSetOf<String>()
    private var rewarded: CachedAd<RewardedAd>? = null
    private var loadingRewarded = false

    private val _canRequestAds = MutableStateFlow(consentInformation.canRequestAds())
    private val _privacyOptionsRequired = MutableStateFlow(false)
    private val _rewardedReady = MutableStateFlow(false)

    val isSubscribed: StateFlow<Boolean> = settingsRepository.getSubscriptionStatus()
        .map { it == true }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.Eagerly, false)

    val adsEnabled: StateFlow<Boolean> = combine(isSubscribed, _canRequestAds) { subscribed, canRequest ->
        !subscribed && canRequest
    }.stateIn(scope, SharingStarted.Eagerly, false)

    val privacyOptionsRequired: StateFlow<Boolean> = _privacyOptionsRequired.asStateFlow()

    val rewardedReady: StateFlow<Boolean> = _rewardedReady.asStateFlow()

    val rewardedSupported: Boolean
        get() = AdUnits.chatRewarded.isNotBlank()

    init {
        scope.launch {
            adsEnabled.collect { enabled ->
                if (enabled) {
                    preload()
                } else {
                    interstitials.clear()
                    rewarded = null
                    _rewardedReady.value = false
                }
            }
        }
    }

    fun start(activity: Activity) {
        if (consentRequested.compareAndSet(false, true)) {
            val params = ConsentRequestParameters.Builder().build()
            consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { error ->
                        error?.let { Log.w(TAG, "Consent form: ${it.message}") }
                        onConsentResolved()
                    }
                },
                { error ->
                    Log.w(TAG, "Consent update failed: ${error.message}")
                    onConsentResolved()
                },
            )
        }
        if (consentInformation.canRequestAds()) startSdk()
    }

    fun showPrivacyOptions(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { error ->
            error?.let { Log.w(TAG, "Privacy options: ${it.message}") }
            onConsentResolved()
        }
    }

    private fun onConsentResolved() {
        _privacyOptionsRequired.value =
            consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        _canRequestAds.value = consentInformation.canRequestAds()
        if (_canRequestAds.value) startSdk()
    }

    private fun startSdk() {
        if (!sdkStarted.compareAndSet(false, true)) return
        scope.launch {
            withContext(Dispatchers.IO) {
                MobileAds.initialize(context) {}
            }
            _canRequestAds.value = consentInformation.canRequestAds()
            preload()
        }
    }

    private fun preload() {
        if (!adsEnabled.value || !sdkStarted.get()) return
        InterstitialPlacement.entries.forEach { loadInterstitial(it.unitId) }
        loadRewarded()
    }

    private fun loadInterstitial(unitId: String) {
        if (unitId.isBlank() || unitId in loadingInterstitials) return
        if (interstitials[unitId]?.isFresh() == true) return
        loadingInterstitials += unitId
        InterstitialAd.load(context, unitId, AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                loadingInterstitials -= unitId
                interstitials[unitId] = CachedAd(ad)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                loadingInterstitials -= unitId
                interstitials.remove(unitId)
                Log.d(TAG, "Interstitial failed to load: ${error.message}")
            }
        })
    }

    private fun loadRewarded() {
        val unitId = AdUnits.chatRewarded
        if (unitId.isBlank() || loadingRewarded || rewarded?.isFresh() == true) return
        loadingRewarded = true
        RewardedAd.load(context, unitId, AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                loadingRewarded = false
                rewarded = CachedAd(ad)
                _rewardedReady.value = true
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                loadingRewarded = false
                rewarded = null
                _rewardedReady.value = false
                Log.d(TAG, "Rewarded failed to load: ${error.message}")
            }
        })
    }

    private fun installedRecently(): Boolean = runCatching {
        val installedAt = context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
        System.currentTimeMillis() - installedAt < NEW_USER_GRACE_MS
    }.getOrDefault(false)

    private fun interstitialAllowed(): Boolean {
        if (!adsEnabled.value) return false
        if (interstitialsThisSession >= MAX_INTERSTITIALS_PER_SESSION) return false
        val now = SystemClock.elapsedRealtime()
        if (now - sessionStartedAt < SESSION_WARM_UP_MS) return false
        if (lastFullScreenAt != 0L && now - lastFullScreenAt < INTERSTITIAL_COOLDOWN_MS) return false
        return !installedRecently()
    }

    fun showInterstitial(activity: Activity, placement: InterstitialPlacement): Boolean {
        if (!interstitialAllowed()) return false
        val unitId = placement.unitId
        val cached = interstitials[unitId]
        if (cached == null || !cached.isFresh()) {
            interstitials.remove(unitId)
            loadInterstitial(unitId)
            return false
        }
        interstitials.remove(unitId)
        cached.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                lastFullScreenAt = SystemClock.elapsedRealtime()
                interstitialsThisSession++
            }

            override fun onAdDismissedFullScreenContent() {
                loadInterstitial(unitId)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(TAG, "Interstitial failed to show: ${error.message}")
                loadInterstitial(unitId)
            }
        }
        cached.ad.show(activity)
        return true
    }

    fun showRewarded(activity: Activity, onRewarded: () -> Unit): Boolean {
        val cached = rewarded
        if (!adsEnabled.value || cached == null || !cached.isFresh()) {
            rewarded = null
            _rewardedReady.value = false
            loadRewarded()
            return false
        }
        rewarded = null
        _rewardedReady.value = false
        cached.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                lastFullScreenAt = SystemClock.elapsedRealtime()
            }

            override fun onAdDismissedFullScreenContent() {
                loadRewarded()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(TAG, "Rewarded failed to show: ${error.message}")
                loadRewarded()
            }
        }
        cached.ad.show(activity) { onRewarded() }
        return true
    }

    private class CachedAd<T>(val ad: T, private val loadedAt: Long = SystemClock.elapsedRealtime()) {
        fun isFresh(): Boolean = SystemClock.elapsedRealtime() - loadedAt < AD_EXPIRY_MS
    }

    private companion object {
        const val TAG = "Ads"
        const val AD_EXPIRY_MS = 55 * 60 * 1000L
        const val INTERSTITIAL_COOLDOWN_MS = 4 * 60 * 1000L
        const val SESSION_WARM_UP_MS = 90 * 1000L
        const val NEW_USER_GRACE_MS = 24 * 60 * 60 * 1000L
        const val MAX_INTERSTITIALS_PER_SESSION = 3
    }
}
