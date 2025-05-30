package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.DeleteTestResultsDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsResponse
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.TestProgress
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class TestResultsViewModel
@Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val database: AppDatabase,
    private val snackBarViewModel: SnackBarViewModel
) : ViewModel() {

    private var tickingTime = MutableStateFlow(0L)

    var showDeleteTestDialog = MutableStateFlow(false)
        private  set

    fun updateShowDeleteTestDialog(show: Boolean) {
        showDeleteTestDialog.value = show
    }

    var hasNavigatedTOInformationalScreen = MutableStateFlow(false)
        private  set

    fun updateHasNavigated(hasNavigated: Boolean) {
        hasNavigatedTOInformationalScreen.value = hasNavigated
    }

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

    private var hasResumedTest = false

    val totalDuration = 20 * 60 * 1000L

    val testResultsStatus = MutableStateFlow(
        Results<List<TestResult>>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    val deleteTestResultsStatus = MutableStateFlow(Results.initial<DeleteTestResultsDTO>())
    val snackBarNotification = MutableStateFlow(Results.initial<SnackBarItem>())

    val getClinicsStatus = MutableStateFlow(
        Results<List<Clinic>>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    val searchClinicsStatus = MutableStateFlow(
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

    val ongoingTestStatus = MutableStateFlow(
        Results<TestProgress>(
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
        searchClinics("")
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
                    database.clinicsDao().deleteClinics()
                    database.clinicsDao().insertClinics(dto.clinics)
                }
            }
        }
    }

    fun searchClinics(searchTerm: String) {
        viewModelScope.launch {
           database.clinicsDao().searchClinics(searchTerm = searchTerm).catch {
               it.printStackTrace()
           }.collect{ clinics ->
               searchClinicsStatus.value = Results.success(clinics)
           }

        }
    }

    fun fetchTestResults() {
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

            ongoingTestStatus.value.data?.let {
                completeTestTimer(it)
            }

            remoteRepository.uploadResults(resultsDTO).collect{
                it.data?.result?.let { result ->
                    database.testResultsDao().insertTestResult(result)
                }
                uploadResultsStatus.value = it
            }
        }
    }

    private var job: Job? = null

    private fun startCounter(progress: TestProgress): Deferred<Job> {
        return viewModelScope.async {
            job?.cancel()
            job = CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    delay(1000L)
                    val newTimeSpent = tickingTime.value + 1000L

                    if (newTimeSpent >= totalDuration) {
                        tickingTime.value = totalDuration
                        database.testProgressDao().updateTestProgress(
                            progress.copy(timeSpent = totalDuration)
                        )
                        break
                    } else {
                        tickingTime.value = newTimeSpent
                        database.testProgressDao().updateTestProgress(
                            progress.copy(timeSpent = newTimeSpent)
                        )
                    }
                }
            }
            job!!
        }
    }

    fun startTest() {
        viewModelScope.launch {
            val currentTimeStamp = Clock.System.now().toEpochMilliseconds()
            val progress = TestProgress(
                id = currentTimeStamp,
                startTime = currentTimeStamp,
                timeSpent = 0L,
                active = true
            )
            database.testProgressDao().insertTestProgress(progress)
            startCounter(progress)
        }
    }

     fun getOngoingTest(){
        viewModelScope.launch {
            ongoingTestStatus.value = Results.loading()
            database.testProgressDao().getActiveTestProgress().catch {
                ongoingTestStatus.value =Results.error()
            }.collect{
                if (!hasResumedTest && it != null) {
                   resumeTest(progress = it)
                }
                ongoingTestStatus.value = Results.success(it)
            }
        }
    }

    fun completeTestTimer(progress: TestProgress){
        viewModelScope.launch {
            job?.cancel()
            tickingTime.value = 0L
            database.testProgressDao().updateTestProgress(progress.copy(active = false))
        }
    }

    private fun resumeTest(progress: TestProgress) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val startTime = progress.startTime
        val elapsed = currentTime - startTime
        tickingTime.value = elapsed

        if (elapsed >= totalDuration) {
            hasResumedTest = false
        } else {
            val testState = startCounter(progress)
            hasResumedTest = testState.isActive
        }
    }

    fun deleteTest(testResult: TestResult) {
      viewModelScope.launch {
          deleteTestResultsStatus.value = Results.loading()
//          remoteRepository.deleteResult(testResult.uuid).collect{ results ->
//
//              results.data?.let {
//                  database.testResultsDao().deleteResult(testResult)
//                  showSnackBarNotification(
//                      SnackBarItem(message = it.message)
//                  )
//              }
//
//              results.message?.let { msg ->
//                  showSnackBarNotification(
//                      SnackBarItem(message = msg, isError = true)
//                  )
//              }
//
//              updateShowDeleteTestDialog(false)
//              deleteTestResultsStatus.value = results
//          }
      }
    }



    private fun showSnackBarNotification(snackBarItem: SnackBarItem) {
        viewModelScope.launch {
            delay(100L)
            snackBarNotification.value = Results.success(snackBarItem)
            delay(5000L)
            snackBarNotification.value = Results.initial()
        }
    }
}
