package com.charlesmuchogo.research.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class HistoryPage : Screen {
    @Composable
    override fun Content() {
        CenteredColumn(modifier = Modifier) {
            Text(text = "History Page")
        }
    }
}

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val testResultsStatus =
        testResultsViewModel.testResultsStatus.collectAsStateWithLifecycle().value
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val user = authenticationViewModel.profileStatus.collectAsStateWithLifecycle().value

    val hideResults = user.data?.hideResults ?: true

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        when (testResultsStatus.status) {
            ResultStatus.INITIAL,
            ResultStatus.LOADING,
            -> {
                CenteredColumn(modifier = Modifier) {
                    CircularProgressIndicator()
                }
            }

            ResultStatus.SUCCESS -> {
                if (testResultsStatus.data.isNullOrEmpty() || hideResults) {
                    CenteredColumn(modifier = Modifier) {
                        Text(text = "No results at the moment")
                    }
                } else {
                    TestResultsListView(results = testResultsStatus.data)
                }
            }

            ResultStatus.ERROR -> {
                CenteredColumn(modifier = Modifier) {
                    Text(text = testResultsStatus.message.toString())
                }
            }
        }
    }
}

@Composable
fun TestResultsListView(
    modifier: Modifier = Modifier,
    results: List<TestResult>,
) {
    LazyColumn(modifier = modifier.padding(horizontal = 12.dp)) {
        items(results) { result ->
            RoutesCard(result = result)
        }
    }
}
