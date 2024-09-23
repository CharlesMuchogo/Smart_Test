package com.charlesmuchogo.research.presentation.testpage

import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.TestProgress
import com.charlesmuchogo.research.presentation.utils.ImagePicker

class SingleTestPage : Screen {
    @Composable
    override fun Content() {
        CenteredColumn {
            Text(text = "Single Test Page")
        }
    }
}

@Composable
fun SingleTestScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
    val context = LocalContext.current
    val activity = LocalContext.current as ComponentActivity
    val imagePicker = ImagePicker(context, activity)
    val clinicsStatus = testResultsViewModel.getClinicsStatus.collectAsStateWithLifecycle().value



    var selectedClinic by remember {
        mutableStateOf<Clinic?>(null)
    }

    var resultImage by remember {
        mutableStateOf<ByteArray?>(null)
    }

    imagePicker.RegisterPicker(onImagePicked = { image ->
        resultImage = image
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(vertical = 24.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        onClick = { imagePicker.captureImage() },
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    )
            ) {
                if (resultImage != null) {
                    val bitmap = BitmapFactory.decodeByteArray(resultImage, 0, resultImage!!.size)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured test image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }else{

                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center) {
                        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Take photo of the test")
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
                        selectedClinic = it
                    }) {
                    Text(it.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        item {
            AppButton(onClick = { /*TODO*/ }) {
                Text(text = "Submit results")
            }
        }
    }
}
