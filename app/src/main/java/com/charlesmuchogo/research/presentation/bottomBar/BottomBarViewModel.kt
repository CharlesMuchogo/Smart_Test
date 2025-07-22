package com.charlesmuchogo.research.presentation.bottomBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(BottomBarState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = BottomBarState()
    )

    fun onAction(action: BottomBarAction){
        when(action){

            is BottomBarAction.OnUpdateSelectedItem -> {
                _state.update { it.copy(selectedBottomBarItem = action.item) }
            }

            is BottomBarAction.OnUpdateSelectedLanguage -> {
                _state.update { it.copy(selectedKiswahiliLanguage = action.isKiswahili) }
            }

            is BottomBarAction.OnHasShownAd -> {
                _state.update { it.copy(showAd = !it.showAd) }
            }
        }

    }

}