package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionUpsellViewModel @Inject constructor(
    private val settingsRepository: MultiplatformSettingsRepository,
) : ViewModel() {

    val isSubscribed: StateFlow<Boolean> = settingsRepository.getSubscriptionStatus()
        .map { it == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false,
        )

    // Defaults to true so the sheet never flashes on screen before the persisted value loads.
    val upsellShown: StateFlow<Boolean> = settingsRepository.getSubscriptionUpsellShown()
        .map { it == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = true,
        )

    fun onUpsellDismissed() {
        viewModelScope.launch {
            settingsRepository.saveSubscriptionUpsellShown(true)
        }
    }
}
