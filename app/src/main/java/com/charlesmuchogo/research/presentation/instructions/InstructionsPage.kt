package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.navigation.PendingTestPage
import com.charlesmuchogo.research.presentation.navigation.TestPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus


@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    isKiswahiliLanguage: Boolean
) {

    val resultsViewModel = hiltViewModel<TestResultsViewModel>()
    val testResults by resultsViewModel.testResultsStatus.collectAsStateWithLifecycle()



    if (isKiswahiliLanguage) {
        KiswahiliInstructions(onClick = {
            if (testResults.status == ResultStatus.SUCCESS && testResults.data?.firstOrNull { it.status.lowercase() == "pending" } != null) {
                navController.navigate(PendingTestPage)
            } else {
                navController.navigate(TestPage)
            }


        })
    } else {
        EnglishInstructions(onClick = {
            if (testResults.status == ResultStatus.SUCCESS && testResults.data?.firstOrNull { it.status.lowercase() == "pending" } != null) {
                navController.navigate(PendingTestPage)
            } else {
                navController.navigate(TestPage)
            }
        })
    }

}

@Composable
fun RegularBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text, style = MaterialTheme.typography.bodyLarge
            .copy(fontWeight = FontWeight.Normal)
    )
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(fontWeight = FontWeight.SemiBold)
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text, style = style
    )
}

