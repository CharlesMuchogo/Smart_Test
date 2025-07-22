package com.charlesmuchogo.research.presentation.instructions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.presentation.onboarding.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class InstructionsScreenViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(InstructionsState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = InstructionsState()
    )

    fun onAction(action: InstructionsAction){

        when(action){
            InstructionsAction.OnHasShownAd -> {
               _state.update { it.copy(showAd = !it.showAd) }
            }
        }
    }
}