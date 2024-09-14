package com.charlesmuchogo.research.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.models.TestResult
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
    val testResultsStatus = testResultsViewModel.testResultsStatus.collectAsStateWithLifecycle().value

    when (testResultsStatus.status) {
        ResultStatus.INITIAL,
        ResultStatus.LOADING,
        -> {
            CenteredColumn(modifier = Modifier) {
                CircularProgressIndicator()
            }
        }

        ResultStatus.SUCCESS -> {
            if (testResultsStatus.data.isNullOrEmpty()) {
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

@Composable
fun TestResultsListView(
    modifier: Modifier = Modifier,
    results: List<TestResult>,
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
        items(results) { result ->
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(vertical = 4.dp),
                onClick = { },
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                ) {
                    Text(text = result.results)
                    Text(text = result.care_option)
                    Text(text = result.date)
                }
            }
        }
    }
}
