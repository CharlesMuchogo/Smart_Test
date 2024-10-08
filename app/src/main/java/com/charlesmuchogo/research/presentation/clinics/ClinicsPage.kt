package com.charlesmuchogo.research.presentation.clinics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class ClinicsPage: Screen {
    @Composable
    override fun Content() {
        Scaffold { padding ->
            ClinicsScreen(modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun ClinicsScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val clinicsState by testResultsViewModel.getClinicsStatus.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer)){
        when(clinicsState.status){
            ResultStatus.INITIAL,
            ResultStatus.LOADING -> {
                CenteredColumn {
                    CircularProgressIndicator()
                }
            }
            ResultStatus.SUCCESS -> {
                if(clinicsState.data.isNullOrEmpty()){
                    CenteredColumn {
                        Text(text = "No care options available at th moment")
                    }
                } else {
                    ClinicsListView(clinics = clinicsState.data!!)
                }
            }
            ResultStatus.ERROR -> {
                CenteredColumn {
                    Text(text = clinicsState.message.toString())
                }
            }
        }
    }

}

@Composable
fun ClinicsListView(modifier: Modifier = Modifier, clinics: List<Clinic>) {
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp)){
        items(clinics){
            ClinicCard(clinic = it)
        }
    }
}