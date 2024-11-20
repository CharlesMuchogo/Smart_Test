package com.charlesmuchogo.research.presentation.testpage

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.TestProgress
import com.charlesmuchogo.research.presentation.utils.ImagePicker
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.convertMillisecondsToTimeTaken


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

    val timeSpent = ongoingTestStatus.data?.timeSpent?: 0L
    val percentage = (timeSpent.toFloat() / 1_200_000.toFloat()) * 100

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    val color = MaterialTheme.colorScheme.onBackground

    imagePicker.RegisterPicker(onImagePicked = { image ->
        if (selectingPartnerImage) {
            testResultsViewModel.updatePartnerImage(image)
        } else {
            testResultsViewModel.updateUserImage(image)
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                TestProgress(
                    content = convertMillisecondsToTimeTaken(
                        ongoingTestStatus.data?.timeSpent ?: 0L
                    ),
                    counterColor = MaterialTheme.colorScheme.onBackground,
                    radius = 30.dp,
                    mainColor = MaterialTheme.colorScheme.primary,
                    percentage = percentage,
                    onClick = {

                        if(ongoingTestStatus.data == null){
                            scheduleHourlyNotificationWork(context = context)
                            testResultsViewModel.startTest()
                        }else{
                            testResultsViewModel.completeTestTimer(ongoingTestStatus.data)
                        }

                    }
                )
            }

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(vertical = 24.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = color,
                                style = stroke
                            )
                        }
                        .clickable(
                            onClick = {
                                testResultsViewModel.updateSelectingPartnerImage(false)
                                imagePicker.captureImage()
                            },
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        )
                ) {
                    if (userImage != null) {
                        val bitmap =
                            BitmapFactory.decodeByteArray(userImage, 0, userImage.size)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured test image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Take photo of my test")
                        }
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(vertical = 24.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = color,
                                style = stroke
                            )
                        }
                        .clickable(
                            onClick = {
                                testResultsViewModel.updateSelectingPartnerImage(true)
                                imagePicker.captureImage()
                            },
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        )
                ) {
                    if (partnerImage != null) {
                        val bitmap =
                            BitmapFactory.decodeByteArray(partnerImage, 0, partnerImage.size)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured test image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Take photo of my partner test",
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }

            }

            item {
                clinicsStatus.data?.let { clinics ->
                    AppDropDown(
                        options = clinics,
                        label = { Text(text = "Select a clinic") },
                        selectedOption = TextFieldState(
                            text = selectedClinic?.name ?: "Select a clinic ",
                            isSelected = selectedClinic != null,
                            error = null
                        ),
                        onOptionSelected = {
                            testResultsViewModel.updateSelectedClinic(it)
                        }) {
                        Text(
                            "${it.name} - ${it.address}",
                            style = MaterialTheme.typography.bodyLarge
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
                    enabled = timeSpent > 1_200_000L,
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
                            AppLoginButtonContent(message = "Submitting...")
                        }

                        ResultStatus.INITIAL,
                        ResultStatus.SUCCESS,
                        ResultStatus.ERROR -> {
                            Text(text = "Submit results")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
