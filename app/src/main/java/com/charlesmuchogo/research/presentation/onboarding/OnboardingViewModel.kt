package com.charlesmuchogo.research.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: MultiplatformSettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = OnboardingState()
    )

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.SaveFirstTime -> {
                settingsRepository.saveFirstTimeUse(firstTime = action.isFirstTime)
            }

            is OnboardingAction.OnChangeSlide -> {
                _state.update { it.copy(currentSlide = action.slide) }
            }
        }
    }

}