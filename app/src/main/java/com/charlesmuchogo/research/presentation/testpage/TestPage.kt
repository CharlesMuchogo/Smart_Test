package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.TestInfoPage
import com.charlesmuchogo.research.navigation.TestPage
import com.charlesmuchogo.research.navigation.InstructionsPage
import com.charlesmuchogo.research.presentation.messages.components.SegmentedTabs
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val currentTab = testResultsViewModel.currentTab.collectAsStateWithLifecycle().value
    val uploadTestState = testResultsViewModel.uploadResultsStatus.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(initialPage = currentTab) { 2 }


    LaunchedEffect(key1 = Unit) {
        testResultsViewModel.getOngoingTest()
    }

    LaunchedEffect(key1 = uploadTestState.status) {
        if(uploadTestState.status == ResultStatus.SUCCESS){
            navController.popBackStack(TestPage, inclusive = true)
            navController.navigate(TestInfoPage)
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.back))
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.takeTest),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                )
            },
            actions = {
                IconButton(onClick = { navController.navigate(InstructionsPage) }) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = stringResource(R.string.how_it_works))
                }
            },
        )
    }) { values ->
        Column(modifier = Modifier
            .padding(values)
            .fillMaxSize()) {
            SegmentedTabs(
                labels = listOf(stringResource(R.string.just_me), stringResource(R.string.with_my_partner)),
                selectedIndex = currentTab,
                onSelect = { testResultsViewModel.updateCurrentTab(tab = it) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )

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
