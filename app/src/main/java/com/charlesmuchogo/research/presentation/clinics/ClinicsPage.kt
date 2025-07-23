package com.charlesmuchogo.research.presentation.clinics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.BannerAd
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.BANNER_AD_UNIT_ID
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.utils.ResultStatus



@Composable
fun ClinicsScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val clinicsState by testResultsViewModel.getClinicsStatus.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer)){
        when(clinicsState.status){
            ResultStatus.INITIAL,
            ResultStatus.LOADING -> {
                CenteredColumn(modifier = Modifier.weight(1f)) {
                    CircularProgressIndicator()
                }
            }
            ResultStatus.SUCCESS -> {
                if(clinicsState.data.isNullOrEmpty()){
                    CenteredColumn(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.noClinicsAtTheMoment) )
                    }
                } else {
                    ClinicsListView(clinics = clinicsState.data!!, modifier = Modifier.weight(1f))
                }
            }
            ResultStatus.ERROR -> {
                CenteredColumn(modifier = Modifier.weight(1f)) {
                    Text(text = clinicsState.message.toString())
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

@Composable
fun ClinicsListView(modifier: Modifier = Modifier, clinics: List<Clinic>) {
    LazyColumn(modifier = modifier
        .padding(horizontal = 8.dp)){
        items(clinics){
            ClinicCard(clinic = it)
        }
    }
}