package com.charlesmuchogo.research.presentation.testpage

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.TestProgress
import com.charlesmuchogo.research.presentation.utils.ImagePicker
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class CoupleTestPage : Screen {
    @Composable
    override fun Content() {
        CenteredColumn {
            Text(text = "Couples Test Page")
        }
    }
}

@Composable
fun CoupleTestScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val context = LocalContext.current
    val imagePicker = ImagePicker(context)
    val clinicsStatus = testResultsViewModel.getClinicsStatus.collectAsStateWithLifecycle().value
    val uploadResultsStatus =
        testResultsViewModel.uploadResultsStatus.collectAsStateWithLifecycle().value

    val userImage = testResultsViewModel.userImage.collectAsStateWithLifecycle().value
    val partnerImage = testResultsViewModel.partnerImage.collectAsStateWithLifecycle().value
    val selectingPartnerImage =  testResultsViewModel.selectingPartnerImage.collectAsStateWithLifecycle().value
    val selectedClinic =   testResultsViewModel.selectedClinic.collectAsStateWithLifecycle().value





    imagePicker.RegisterPicker(onImagePicked = { image ->
        if (selectingPartnerImage) {
            testResultsViewModel.updatePartnerImage(image)
        } else {
            testResultsViewModel.updateUserImage(image)
        }
    })

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            TestProgress(
                content = "20:00",
                counterColor = MaterialTheme.colorScheme.onBackground,
                radius = 30.dp,
                mainColor = MaterialTheme.colorScheme.primary,
                percentage = 10f,
                onClick = {}
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 24.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
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
                        .weight(1f)
                        .padding(vertical = 24.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
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
                    Text("${it.name} - ${it.address}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        item {
            uploadResultsStatus.message?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        item {
            AppButton(onClick = {
                if (userImage != null && partnerImage != null) {
                    testResultsViewModel.updateResults(
                        UploadTestResultsDTO(
                            image = userImage,
                            partnerImage = partnerImage,
                            careOption = selectedClinic?.id
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
    }
}
