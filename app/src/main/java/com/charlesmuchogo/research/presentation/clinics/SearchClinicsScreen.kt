package com.charlesmuchogo.research.presentation.clinics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.ads.BannerAd
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.BANNER_AD_UNIT_ID
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.SearchBar
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchClinicsScreen(modifier: Modifier = Modifier, navController: NavController) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val searchClinicsState by testResultsViewModel.searchClinicsStatus.collectAsStateWithLifecycle()
    val searchQuery = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }


    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .onGloballyPositioned {
                                focusRequester.requestFocus()
                            }
                            .padding(4.dp),
                ) {
                    SearchBar(
                        description = "Search Clinics",
                        value = searchQuery.value,
                        onValueChange = { newText ->
                            searchQuery.value = newText
                            testResultsViewModel.searchClinics(searchQuery.value)
                        },
                        modifier =
                            Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                                .padding(end = 4.dp, top = 4.dp, bottom = 4.dp),
                        onExit = { navController.popBackStack() },
                    )
                }
            },
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (searchClinicsState.status) {
                ResultStatus.INITIAL, ResultStatus.LOADING -> {
                    CenteredColumn(modifier = Modifier.weight(1f)) {
                        Text("Type to search a clinic...")
                    }
                }

                ResultStatus.ERROR -> {
                    CenteredColumn(modifier = Modifier.weight(1f)) {
                        Text("Something went wrong.. Try something else")
                    }
                }

                ResultStatus.SUCCESS -> {
                    when (searchClinicsState.data.isNullOrEmpty()) {
                        true -> {
                            CenteredColumn(modifier = Modifier.weight(1f)) {
                                Text("No clinics found... Try something else")
                            }
                        }

                        else -> {
                            ClinicsListView(
                                modifier = Modifier
                                    .weight(1f),
                                clinics = searchClinicsState.data ?: emptyList()
                            )
                        }
                    }
                }
            }

            Box(modifier = modifier.fillMaxWidth()) {
                BannerAd(
                    modifier = Modifier,
                    adUnitId = BANNER_AD_UNIT_ID
                )
            }
        }
    }
}