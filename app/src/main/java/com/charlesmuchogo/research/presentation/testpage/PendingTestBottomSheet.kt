package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTestScreen(navController: NavController) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()

    LaunchedEffect(key1 = true) {
        testResultsViewModel.updateHasNavigated(true)
    }

    DisposableEffect(key1 =true) {
        onDispose {
            testResultsViewModel.updateHasNavigated(false)
        }
    }
    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {

            IconButton(onClick = {
                navController.popBackStack()
                navController.popBackStack()

            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    Spacer(modifier = Modifier.fillMaxSize(0.3f))
                }
                item {
                    Image(
                        painter = painterResource(id = R.drawable.pending),
                        modifier = Modifier
                            .fillParentMaxWidth(0.4f)
                            .fillParentMaxHeight(0.3f),
                        contentDescription = "picture"
                    )
                }


                item {
                    Text(
                        text = "Your test was submitted successfully",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                    )
                }

                item {
                    Text(text = "Please wait for the approval of your results")
                }
            }
        }
    }
}
