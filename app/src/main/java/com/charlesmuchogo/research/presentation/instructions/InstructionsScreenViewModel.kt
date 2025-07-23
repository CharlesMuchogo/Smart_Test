package com.charlesmuchogo.research.presentation.instructions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.presentation.onboarding.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class InstructionsScreenViewModel @Inject constructor(private val  database: AppDatabase): ViewModel() {

    private val _state = MutableStateFlow(InstructionsState())
    val state = _state.combine(database.testResultsDao().getTestResults()){ currentState, results ->
        currentState.copy(
            hasPendingResults = results.any { it.status.lowercase() == "pending" }
        )
    }.stateIn(
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