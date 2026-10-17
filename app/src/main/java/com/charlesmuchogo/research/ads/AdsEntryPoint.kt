package com.charlesmuchogo.research.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AdsEntryPoint {
    fun adsManager(): AdsManager
}

@Composable
fun rememberAdsManager(): AdsManager {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        EntryPointAccessors.fromApplication(context, AdsEntryPoint::class.java).adsManager()
    }
}
