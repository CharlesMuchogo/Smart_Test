package com.charlesmuchogo.research.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.domain.models.TestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeState(
    val firstName: String? = null,
    val isSignedIn: Boolean = false,
    val latestResult: TestResult? = null,
    val hasPendingResults: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(database: AppDatabase) : ViewModel() {

    val state: StateFlow<HomeState> = combine(
        database.userDao().getUser(),
        database.testResultsDao().getTestResults(),
    ) { user, results ->
        HomeState(
            firstName = user?.firstName?.takeIf { it.isNotBlank() },
            isSignedIn = user != null,
            latestResult = results.firstOrNull(),
            hasPendingResults = results.any { it.status.lowercase() == "pending" },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState(),
    )
}
