package com.charlesmuchogo.research.presentation.results

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.AccountRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.io.File
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
    private val accountRepository: AccountRepository,
    private val snackBarViewModel: SnackBarViewModel,
    private val database: AppDatabase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _events = Channel<ResultsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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
                    launch { refreshResultsFromServer() }
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

                is ResultsAction.OnExportPdf -> exportPdf(action.share)

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

    private suspend fun exportPdf(share: Boolean) {
        val testResult = _state.value.result ?: return
        if (_state.value.isExportingPdf) return
        if (testResult.uuid.isBlank()) {
            snackBarViewModel.sendEvent(SnackBarItem(message = context.getString(R.string.pdf_not_ready), isError = true))
            return
        }
        _state.update { it.copy(isExportingPdf = true) }
        val result = accountRepository.downloadResultsPdf(testResult.uuid)
        val bytes = result.data
        if (bytes == null) {
            _state.update { it.copy(isExportingPdf = false) }
            snackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: context.getString(R.string.pdf_failed), isError = true))
            return
        }
        val file = runCatching {
            withContext(Dispatchers.IO) {
                val directory = File(context.cacheDir, "results").apply { mkdirs() }
                File(directory, "smart-test-results-${testResult.uuid}.pdf").apply { writeBytes(bytes) }
            }
        }.getOrNull()
        _state.update { it.copy(isExportingPdf = false) }
        if (file == null) {
            snackBarViewModel.sendEvent(SnackBarItem(message = context.getString(R.string.pdf_failed), isError = true))
            return
        }
        _events.send(ResultsEvent.OpenPdf(file, share))
    }

    private suspend fun refreshResultsFromServer() {
        remoteRepository.fetchTestResults().collect { response ->
            response.data?.results?.forEach { database.testResultsDao().insertTestResult(result = it) }
        }
    }
}
