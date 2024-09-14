package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.TestProgress

class CoupleTestPage : Screen {
    @Composable
    override fun Content() {
        CenteredColumn {
            Text(text = "Couples Test Page")
        }
    }
}

@Composable
fun CoupleTestScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
        item {
            TestProgress(
                content = "20:00",
                counterColor = MaterialTheme.colorScheme.onBackground,
                radius = 30.dp,
                mainColor = MaterialTheme.colorScheme.primary,
                percentage = 10f,
                onClick = {}
            )
        }
    }
}
