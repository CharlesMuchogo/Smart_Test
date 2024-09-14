package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestResultsViewModel
    @Inject
    constructor(
        private val remoteRepository: RemoteRepository,
        private val database: AppDatabase,
    ) : ViewModel() {
        private val _currentTab = MutableStateFlow(0)
        val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

        fun updateCurrentTab(tab: Int) {
            _currentTab.value = tab
        }

        val testResultsStatus =
            MutableStateFlow(
                Results<List<TestResult>>(
                    data = null,
                    message = null,
                    status = ResultStatus.INITIAL,
                ),
            )

        init {
            getTestResults()
            fetchTestResults()
        }

        private fun fetchTestResults() {
            viewModelScope.launch {
                remoteRepository.fetchTestResults().collect {
                    it.data?.let { results ->
                        results.results.forEach { result ->
                            database.testResultsDao().insertTestResult(result = result)
                        }
                    }
                }
            }
        }

        private fun getTestResults() {
            viewModelScope.launch {
                testResultsStatus.value = Results.loading()
                database
                    .testResultsDao()
                    .getTestResults()
                    .catch {
                        testResultsStatus.value = Results.error()
                    }.collect {
                        testResultsStatus.value = Results.success(it)
                    }
            }
        }
    }
