package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.navigation.MoreDetailsPage
import com.charlesmuchogo.research.navigation.RegistrationPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.TERMS_AND_CONDITIONS_URL
import com.charlesmuchogo.research.presentation.utils.getDeviceCountry
import com.charlesmuchogo.research.presentation.utils.openInAppBrowser

@Composable
fun RegistrationScreen(modifier: Modifier = Modifier, navController: NavController) {

    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val registrationStatus by authenticationViewModel.registrationStatus.collectAsState()
    val registrationPageState = authenticationViewModel.loginPageState
    val  context = LocalContext.current

    LaunchedEffect(Unit) {
        val country = getDeviceCountry(context)
        authenticationViewModel.onAction(LoginAction.OnCountryChange(country))
    }

    Scaffold { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier =
            modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Create an account",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = SemiBold, letterSpacing = 1.5.sp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Please enter your details to continue.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Normal),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically ,modifier = Modifier.fillMaxWidth()){
                    AppTextField(
                        modifier = Modifier.weight(1f),
                        label = "First Name",
                        value = registrationPageState.firstName,
                        placeholder = "John",
                        error = registrationPageState.firstNameError,
                        onValueChanged = { authenticationViewModel.onAction(LoginAction.OnFirstNameChange(it))},
                        keyboardType = KeyboardType.Text,
                    )

                    AppTextField(
                        modifier = Modifier.weight(1f),
                        label = "Last Name",
                        value = registrationPageState.lastname,
                        placeholder = "Doe",
                        error = registrationPageState.lastnameError,
                        onValueChanged = { authenticationViewModel.onAction(LoginAction.OnLastNameChange(it)) },
                        keyboardType = KeyboardType.Text,
                    )
                }
            }


            item {
                AppTextField(
                    label = "Phone Number",
                    value = registrationPageState.phoneNumber,
                    placeholder = "07123456789",
                    error = registrationPageState.phoneNumberError,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnPhoneNumberChange(it))},
                    keyboardType = KeyboardType.Phone,
                )
            }

            item {
                AppTextField(
                    label = "Email",
                    value = registrationPageState.email,
                    placeholder = "johndoe@email.com",
                    error = registrationPageState.emailError,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnEmailChange(it)) },
                    keyboardType = KeyboardType.Email,
                )
            }

            item {
                AppTextField(
                    label = "Password",
                    value = registrationPageState.password,
                    placeholder = "*********",
                    error = registrationPageState.passwordError,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnPasswordChange(it))},
                    keyboardType = KeyboardType.Password,
                    passwordVisible = registrationPageState.showPassword,
                    imeAction = ImeAction.Next,
                    trailingIcon = {
                        IconButton(onClick = {
                            authenticationViewModel.onAction(LoginAction.OnShowPasswordChange(!registrationPageState.showPassword))
                        }) {
                            Icon(
                                imageVector = if (registrationPageState.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "show password",
                            )
                        }
                    },
                )
            }

            item {
                AppTextField(
                    label = "Confirm Password",
                    value = registrationPageState.confirmPassword,
                    placeholder = "*********",
                    error = registrationPageState.confirmPasswordError,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnConfirmPasswordChange(it)) },
                    keyboardType = KeyboardType.Password,
                    passwordVisible = registrationPageState.showPassword,
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(onClick = {
                            authenticationViewModel.onAction(LoginAction.OnShowPasswordChange(!registrationPageState.showPassword))
                        }) {
                            Icon(
                                imageVector = if (registrationPageState.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "show password",
                            )
                        }
                    },
                )
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(
                        checked = registrationPageState.termsAndConditions,
                        onCheckedChange = { authenticationViewModel.onAction(LoginAction.OnTermsAndConditionsChange(agree = !registrationPageState.termsAndConditions)) },
                    )
                    Text("I agree to the ", style = MaterialTheme.typography.bodyMedium)
                    Text(modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            openInAppBrowser(context = context, url = TERMS_AND_CONDITIONS_URL)
                        }
                    ), text = "Terms and Conditions", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = SemiBold, color = MaterialTheme.colorScheme.primary,textDecoration = TextDecoration.Underline))
                }
            }

            item {
                registrationStatus.message?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }

                AppButton(
                    enabled = registrationPageState.termsAndConditions,
                    onClick = {
                        authenticationViewModel.onAction(LoginAction.OnSignup)
                    },
                    content = {
                        when (registrationStatus.status) {
                            ResultStatus.INITIAL,
                            ResultStatus.ERROR -> {
                                Text("Sign up")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Sign up")
                                navController.navigate(MoreDetailsPage){
                                    popUpTo(RegistrationPage) {
                                        inclusive = true
                                    }
                                }
                            }

                            ResultStatus.LOADING -> {
                                AppLoginButtonContent(message = "Signing up ...")
                            }
                        }
                    },
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Have an account?", style = MaterialTheme.typography.bodyLarge)
                    TextButton(onClick = { navController.popBackStack()}) {
                        Text(text = "Sign in", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

