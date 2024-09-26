package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsResponse
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestResultsViewModel
@Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val database: AppDatabase,
) : ViewModel() {


    var currentTab = MutableStateFlow(0)
        private  set

    fun updateCurrentTab(tab: Int) {
        currentTab.value = tab
    }

    var userImage = MutableStateFlow<ByteArray?>(null)
        private set

    fun updateUserImage(image: ByteArray?) {
        userImage.value = image
    }

    var partnerImage = MutableStateFlow<ByteArray?>(null)
        private set

    fun updatePartnerImage(image: ByteArray?) {
        partnerImage.value = image
    }

    var selectingPartnerImage = MutableStateFlow(false)
        private set

    fun updateSelectingPartnerImage(selecting: Boolean) {
        selectingPartnerImage.value = selecting
    }

    var selectedClinic = MutableStateFlow<Clinic?>(null)
        private set

    fun updateSelectedClinic(clinic: Clinic?) {
        selectedClinic.value = clinic
    }





    val testResultsStatus = MutableStateFlow(
        Results<List<TestResult>>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    val getClinicsStatus = MutableStateFlow(
        Results<List<Clinic>>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    val uploadResultsStatus = MutableStateFlow(
        Results<UploadTestResultsResponse>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    init {
        getTestResults()
        getClinics()
        fetchTestResults()
        fetchClinics()
    }

    private fun getClinics() {
        viewModelScope.launch {
            getClinicsStatus.value = Results.loading()
            database.clinicsDao().getClinics().catch {
                getClinicsStatus.value = Results.error()
            }.collect {
                getClinicsStatus.value = Results.success(it)
            }
        }
    }

    private fun fetchClinics() {
        viewModelScope.launch {
            remoteRepository.fetchClinics().collect {
                it.data?.let { dto ->
                    database.clinicsDao().insertClinics(dto.clinics)
                }
            }
        }
    }

    private fun fetchTestResults() {
        viewModelScope.launch {
            remoteRepository.fetchTestResults().collect {
                it.data?.let { results ->
                    results.results.forEach { result ->
                        database.testResultsDao().insertTestResult(result = result)
                    }
                }
            }
        }
    }

    private fun getTestResults() {
        viewModelScope.launch {
            testResultsStatus.value = Results.loading()
            database
                .testResultsDao()
                .getTestResults()
                .catch {
                    testResultsStatus.value = Results.error()
                }.collect {
                    testResultsStatus.value = Results.success(it)
                }
        }
    }

    fun updateResults(resultsDTO: UploadTestResultsDTO){
        viewModelScope.launch {
            uploadResultsStatus.value = Results.loading()
            remoteRepository.uploadResults(resultsDTO).collect{
                uploadResultsStatus.value = it
            }
        }
    }
}
