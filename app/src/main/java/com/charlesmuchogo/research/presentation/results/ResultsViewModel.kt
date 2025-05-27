package com.charlesmuchogo.research.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
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
    private val snackBarViewModel: SnackBarViewModel,
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

                is ResultsAction.OnShowDeleteDialog -> {
                    _state.update { it.copy(showDeleteDialog = action.show) }
                }

                is ResultsAction.OnShowMoreChange -> {
                    _state.update { it.copy(showMore = action.show) }
                }

                ResultsAction.OnDeleteResults -> {


                    _state.value.result?.let { testResult ->
                        _state.update { it.copy(isDeleting = true, showDeleteDialog = false) }

                        val result = remoteRepository.deleteResult(testResult.uuid)

                        result.message?.let { msg ->
                            _state.update { it.copy(isDeleting = false, hasDeleted = false) }
                            snackBarViewModel.sendEvent(SnackBarItem(message = msg, isError = false))
                        }

                        result.data?.let {
                            snackBarViewModel.sendEvent(SnackBarItem(message = it.message, isError = true))
                            _state.update { it.copy(isDeleting = false, hasDeleted = true) }
                            database.testResultsDao().deleteResult(testResult)
                        }
                    }

                }
            }
        }
    }
}