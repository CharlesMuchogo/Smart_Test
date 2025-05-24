package com.charlesmuchogo.research.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val database: AppDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(ResultsState())
    val state = _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ResultsState()
        )

    fun onAction(action: ResultsAction) {
        viewModelScope.launch {
            when (action) {
                is ResultsAction.OnLoadResult -> {
                    database.testResultsDao().getTestResult(action.id).catch {
                        it.printStackTrace()
                    }.collect { result ->
                        _state.update { it.copy(result = result) }
                    }
                }
            }
        }
    }
}