package com.charlesmuchogo.research.ads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.SubscribePage
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

enum class BannerStyle { ANCHORED, INLINE }

private const val INLINE_MAX_HEIGHT_DP = 250

@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
    style: BannerStyle = BannerStyle.ANCHORED,
    showRemoveAds: Boolean = true,
) {
    val adsManager = rememberAdsManager()
    val enabled by adsManager.adsEnabled.collectAsStateWithLifecycle()
    if (!enabled) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val widthDp = maxWidth.value.toInt().coerceAtLeast(1)
        AdaptiveBanner(widthDp = widthDp, style = style, showRemoveAds = showRemoveAds)
    }
}

@Composable
private fun AdaptiveBanner(widthDp: Int, style: BannerStyle, showRemoveAds: Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var loaded by remember { mutableStateOf(false) }

    val adSize = remember(widthDp, style) {
        when (style) {
            BannerStyle.ANCHORED -> AdSize.getLargeAnchoredAdaptiveBannerAdSize(context, widthDp)
            BannerStyle.INLINE -> AdSize.getInlineAdaptiveBannerAdSize(widthDp, INLINE_MAX_HEIGHT_DP)
        }
    }

    val adView = remember(adSize) {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = AdUnits.banner
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    loaded = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loaded = false
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(adView, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (loaded && showRemoveAds) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.ad_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(
                    onClick = { navController.navigate(SubscribePage) },
                    modifier = Modifier.heightIn(min = 32.dp),
                ) {
                    Text(
                        text = stringResource(R.string.remove_ads),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (loaded) Modifier else Modifier.height(0.dp)),
            factory = { adView },
        )
    }
}
