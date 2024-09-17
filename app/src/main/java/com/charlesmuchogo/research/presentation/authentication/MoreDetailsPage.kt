package com.charlesmuchogo.research.presentation.authentication

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.bottomBar.HomePage
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.utils.LocalAppNavigator
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class MoreDetailsPage : Screen {
    @Composable
    override fun Content() {
        MoreDetailsScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreDetailsScreen(modifier: Modifier = Modifier) {
    val activity = (LocalContext.current as? Activity)
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val completeRegistrationState = authenticationViewModel.completeRegistrationState.collectAsState().value
    val navigator = LocalAppNavigator.currentOrThrow
    var testedBefore by remember { mutableStateOf(true) }

    val levelsOfEducation = listOf(
        "Primary School",
        "High School",
        "Diploma",
        "Bachelor's Degree",
        "Master's Degree",
        "Doctorate (PhD)",
        "Postdoctoral"
    )

    val genders = listOf(
        "Female",
        "Male",
        "Non-Binary",
        "Prefer not to say"
    )

    var age by remember {
        mutableStateOf("")
    }

    var levelOfEducation by remember {
        mutableStateOf<String?>(null)
    }

    var levelOfEducationError by remember {
        mutableStateOf<String?>(null)
    }
    var selectedGender by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { activity?.finishAndRemoveTask() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
                    }
                },
                title = { Text(text = "Complete Registration") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = "You are one step away from completing your registration. Tell us more about yourself",
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                AppTextField(
                    label = "How old are you?",
                    value = age,
                    onValueChanged = { age = it },
                    error = null,
                    placeholder = "21",
                    keyboardType = KeyboardType.Number,
                )
            }

            item {
                AppDropDown(
                    options = genders,
                    label = { Text(text = "Select your gender") },
                    selectedOption = TextFieldState(
                        text = selectedGender ?: "Select your gender",
                        isSelected = selectedGender.isNullOrBlank()
                    ),
                    onOptionSelected = {
                        selectedGender = it
                    }) {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }

            item {
                AppDropDown(
                    options = levelsOfEducation,
                    label = { Text(text = "What is your education level?") },
                    selectedOption = TextFieldState(
                        text = levelOfEducation ?: "Select your education level",
                        isSelected = levelOfEducation.isNullOrBlank(),
                        error = levelOfEducationError
                    ),
                    onOptionSelected = {
                        levelOfEducation = it
                    }) {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }


            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = testedBefore,
                        onCheckedChange = { isChecked -> testedBefore = isChecked },
                    )
                    Text(
                        "I have tested my HIV status before",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            item {
                AppButton(
                    onClick = {
                        authenticationViewModel.updateUserDetails(
                            details = UpdateUserDetailsDTO(
                                testedBefore = testedBefore,
                                age = age,
                                educationLevel = levelOfEducation ?: "",
                                gender = selectedGender ?: "",
                            )
                        )
                    },
                    content = {
                        when (completeRegistrationState.status) {
                            ResultStatus.INITIAL,
                            ResultStatus.ERROR -> {
                                Text("Continue")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Continue")
                                navigator.replaceAll(HomePage())
                            }

                            ResultStatus.LOADING -> {
                                AppLoginButtonContent(message = "Submitting...")
                            }
                        }
                    },
                )
            }
        }
    }
}