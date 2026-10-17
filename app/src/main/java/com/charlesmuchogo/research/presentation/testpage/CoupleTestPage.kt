package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppLoadingButtonContent
import com.charlesmuchogo.research.presentation.utils.ImagePicker
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@Composable
fun CoupleTestScreen(modifier: Modifier = Modifier, navController: NavController) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val context = LocalContext.current
    val imagePicker = ImagePicker(context)
    val clinicsStatus = testResultsViewModel.getClinicsStatus.collectAsStateWithLifecycle().value
    val uploadResultsStatus =
        testResultsViewModel.uploadResultsStatus.collectAsStateWithLifecycle().value

    val userImage = testResultsViewModel.userImage.collectAsStateWithLifecycle().value
    val partnerImage = testResultsViewModel.partnerImage.collectAsStateWithLifecycle().value
    val selectingPartnerImage =
        testResultsViewModel.selectingPartnerImage.collectAsStateWithLifecycle().value
    val selectedClinic = testResultsViewModel.selectedClinic.collectAsStateWithLifecycle().value
    val ongoingTestStatus =
        testResultsViewModel.ongoingTestStatus.collectAsStateWithLifecycle().value

    val timeSpent = ongoingTestStatus.data?.timeSpent ?: 0L
    val totalTimeDuration = testResultsViewModel.totalDuration
    val started = ongoingTestStatus.data != null
    val timerDone = started && timeSpent >= totalTimeDuration
    val step = currentTestStep(
        started = started,
        timerDone = timerDone,
        photosReady = userImage != null && partnerImage != null,
    )

    imagePicker.RegisterPicker(onImagePicked = { image ->
        if (selectingPartnerImage) {
            testResultsViewModel.updatePartnerImage(image)
        } else {
            testResultsViewModel.updateUserImage(image)
        }
    })

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        item {
            TestTimerCard(
                step = step,
                started = started,
                timeSpent = timeSpent,
                totalDuration = totalTimeDuration,
                onTimerClick = {
                    if (!started) {
                        scheduleHourlyNotificationWork(context = context)
                        testResultsViewModel.startTest()
                    }
                },
                onCancel = {
                    ongoingTestStatus.data?.let { testResultsViewModel.completeTestTimer(it) }
                },
            )
        }

        item { TestStepsList(currentStep = step) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TestPhotoSlot(
                    image = userImage,
                    label = stringResource(R.string.take_photo_of_my_test),
                    enabled = timerDone,
                    onClick = {
                        testResultsViewModel.updateSelectingPartnerImage(false)
                        imagePicker.captureImage()
                    },
                    modifier = Modifier.weight(1f),
                )
                TestPhotoSlot(
                    image = partnerImage,
                    label = stringResource(R.string.take_photo_of_my_partner_test),
                    enabled = timerDone,
                    onClick = {
                        testResultsViewModel.updateSelectingPartnerImage(true)
                        imagePicker.captureImage()
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item {
            clinicsStatus.data?.let { clinics ->
                AppDropDown(
                    options = clinics,
                    label = { Text(text = stringResource(R.string.select_a_clinic)) },
                    selectedOption = TextFieldState(
                        text = selectedClinic?.name ?: stringResource(R.string.select_a_clinic),
                        isSelected = selectedClinic != null,
                        error = null
                    ),
                    onOptionSelected = {
                        testResultsViewModel.updateSelectedClinic(it)
                    }) {
                    Text(
                        "${it.name} - ${it.address}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        item {
            uploadResultsStatus.message?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
                )
            }

            uploadResultsStatus.data?.message?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }

        item {
            AppButton(
                enabled = timerDone && userImage != null && partnerImage != null,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (userImage != null && partnerImage != null) {
                        testResultsViewModel.updateResults(
                            UploadTestResultsDTO(
                                image = userImage,
                                partnerImage = partnerImage,
                                careOption = selectedClinic?.name
                            )
                        )
                    }
                }) {
                when (uploadResultsStatus.status) {
                    ResultStatus.LOADING -> {
                        AppLoadingButtonContent(message = stringResource(R.string.submitting))
                    }

                    ResultStatus.INITIAL,
                    ResultStatus.SUCCESS,
                    ResultStatus.ERROR -> {
                        Text(text = stringResource(R.string.submit), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item { AskDoctorLink() }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
