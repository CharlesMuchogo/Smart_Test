package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.models.TabRowItem
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.navigation.PendingTestPage
import com.charlesmuchogo.research.presentation.navigation.TestInfoPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(modifier: Modifier = Modifier, navController: NavController) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val currentTab = testResultsViewModel.currentTab.collectAsStateWithLifecycle().value
    val hasNavigated = testResultsViewModel.hasNavigatedTOInformationalScreen.collectAsStateWithLifecycle().value
    val testResults = testResultsViewModel.testResultsStatus.collectAsStateWithLifecycle().value
    val uploadTestState = testResultsViewModel.uploadResultsStatus.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(initialPage = currentTab) { TabRowItem.testItems.size }


    LaunchedEffect(key1 = true) {
        testResultsViewModel.getOngoingTest()
    }

    LaunchedEffect(key1 = uploadTestState.status) {
        if(uploadTestState.status == ResultStatus.SUCCESS){
            navController.navigate(TestInfoPage)
        }
    }

    LaunchedEffect(testResults.status) {
        if(testResults.status == ResultStatus.SUCCESS && !hasNavigated && testResults.data?.firstOrNull { it.status.lowercase() == "pending" } != null){
            navController.navigate(PendingTestPage)
            testResultsViewModel.updateHasNavigated(true)
        }
    }

    LaunchedEffect(currentTab) {
        pagerState.animateScrollToPage(currentTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        testResultsViewModel.updateCurrentTab(tab = pagerState.currentPage)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                }
            },
            title = { Text(text = "Take a test") },
        )
    }) { values ->
        Column(modifier = Modifier
            .padding(values)
            .fillMaxSize()) {
            TabRow(
                selectedTabIndex = currentTab,
            ) {
                TabRowItem.testItems.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        selected = index == currentTab,
                        onClick = { testResultsViewModel.updateCurrentTab(tab = index) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (currentTab == index) item.selectedIcon else item.unSelectedIcon,
                                    tint =
                                    if (currentTab ==
                                        index
                                    ) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground
                                    },
                                    contentDescription = item.title,
                                )

                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = item.title,
                                    style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color =
                                        if (currentTab ==
                                            index
                                        ) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onBackground
                                        },
                                    ),
                                )
                            }
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (page) {
                    0 ->
                        SingleTestScreen(
                            modifier = Modifier,
                            navController = navController
                        )

                    1 ->
                        CoupleTestScreen(
                            modifier = Modifier,
                            navController = navController
                        )
                }
            }
        }
    }
}
