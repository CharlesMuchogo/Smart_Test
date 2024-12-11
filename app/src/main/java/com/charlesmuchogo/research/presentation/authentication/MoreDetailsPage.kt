package com.charlesmuchogo.research.presentation.authentication

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDatePickerDialog
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.navigation.HomePage
import com.charlesmuchogo.research.presentation.navigation.MoreDetailsPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.calculateDifferenceBetweenDates
import com.charlesmuchogo.research.presentation.utils.convertTimestampToDate
import com.charlesmuchogo.research.presentation.utils.genders
import com.charlesmuchogo.research.presentation.utils.levelsOfEducation
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreDetailsScreen(modifier: Modifier = Modifier, navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val completeRegistrationState =
        authenticationViewModel.completeRegistrationState.collectAsState().value
    var showDatePicker by remember { mutableStateOf(false) }


    val timestamp18YearsAgo = Clock.System.now().toEpochMilliseconds() - 568_036_800_000L
    val startDate = convertTimestampToDate(timestamp18YearsAgo)

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = timestamp18YearsAgo,
            selectableDates =
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val startDate = convertTimestampToDate(timestamp18YearsAgo )
                    val endDate = convertTimestampToDate(utcTimeMillis)
                    val dateDifference = calculateDifferenceBetweenDates(startDate = startDate, endDate = endDate)
                    return dateDifference <= 0
                }
            },
        )

    val pageState = authenticationViewModel.loginPageState

    AnimatedVisibility(showDatePicker){
        AppDatePickerDialog(
            datePickerState = datePickerState,
            onDismiss = {showDatePicker = false},
            onDateSelected = {
                val date = convertTimestampToDate(it)
                authenticationViewModel.onAction(LoginAction.OnAgeChange(date))
            }
        )
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
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.completeregistration),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.fillParentMaxHeight(0.2f)
                    )
                }
            }

            item {
                Text(
                    text = "You are one step away from completing your registration. Tell us more about yourself",
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                AppTextField(
                    label = "Select your date of birth",
                    value = pageState.age,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnAgeChange(it)) },
                    error = pageState.ageError,
                    placeholder = startDate,
                    keyboardType = KeyboardType.Number,
                    trailingIcon = {
                        IconButton(
                            onClick = {showDatePicker = true}
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, tint = MaterialTheme.colorScheme.primary, contentDescription = "Birthday")
                        }
                    }
                )
            }

            item {
                AppDropDown(
                    options = genders,
                    label = { Text(text = "Select your gender") },
                    selectedOption = TextFieldState(
                        text = pageState.gender.ifBlank { "Select your gender" },
                        isSelected = pageState.gender.isNotBlank(),
                        error = pageState.genderError
                    ),
                    onOptionSelected = {
                        authenticationViewModel.onAction(LoginAction.OnGenderChange(it))
                    }) {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }

            item {
                AppDropDown(
                    options = levelsOfEducation,
                    label = { Text(text = "What is your education level?") },
                    selectedOption = TextFieldState(
                        text =  pageState.educationLevel.ifBlank { "Select your education level" } ,
                        isSelected = pageState.educationLevel.isNotBlank(),
                        error = pageState.educationLevelError
                    ),
                    onOptionSelected = {
                        authenticationViewModel.onAction(LoginAction.OnEducationLevelChange(it))
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
                        checked = pageState.hasTestedBefore,
                        onCheckedChange = { isChecked ->
                            authenticationViewModel.onAction(
                                LoginAction.OnHasTestedBeforeChange(isChecked)
                            )
                        },
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
                        authenticationViewModel.onAction(LoginAction.OnUpdateDetails)

                    },
                    content = {
                        when (completeRegistrationState.status) {
                            ResultStatus.INITIAL,
                            ResultStatus.ERROR -> {
                                Text("Continue")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Continue")
                                navController.navigate(HomePage) {
                                    popUpTo(MoreDetailsPage) {
                                        inclusive = true
                                    }
                                }
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