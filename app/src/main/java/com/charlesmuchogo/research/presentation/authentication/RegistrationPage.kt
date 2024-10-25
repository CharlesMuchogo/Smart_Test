package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.bottomBar.HomePage
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.utils.LocalAppNavigator
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class RegistrationPage : Screen {
    @Composable
    override fun Content() {
        RegistrationScreen(modifier = Modifier)
    }
}

@Composable
fun RegistrationScreen(modifier: Modifier = Modifier) {

    val navigator = LocalAppNavigator.currentOrThrow
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val registrationStatus by authenticationViewModel.registrationStatus.collectAsState()
    val registrationPageState = authenticationViewModel.loginPageState
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier =
            modifier
                .fillMaxSize()
                .padding(padding)
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
                        onValueChanged = { authenticationViewModel.onAction(LoginAction.OnFirstNameChange(it))},
                        error = registrationPageState.firstNameError,
                        placeholder = "John",
                        keyboardType = KeyboardType.Text,
                    )

                    AppTextField(
                        modifier = Modifier.weight(1f),
                        label = "Last Name",
                        value = registrationPageState.lastname,
                        onValueChanged = { authenticationViewModel.onAction(LoginAction.OnLastNameChange(it)) },
                        error = registrationPageState.lastnameError,
                        placeholder = "Doe",
                        keyboardType = KeyboardType.Text,
                    )
                }
            }


            item {
                AppTextField(
                    label = "Phone Number",
                    value = registrationPageState.phoneNumber,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnPhoneNumberChange(it))},
                    error = registrationPageState.phoneNumberError,
                    placeholder = "07123456789",
                    keyboardType = KeyboardType.Phone,
                )
            }

            item {
                AppTextField(
                    label = "Email",
                    value = registrationPageState.email,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnEmailChange(it)) },
                    error = registrationPageState.emailError,
                    placeholder = "johndoe@email.com",
                    keyboardType = KeyboardType.Email,
                )
            }

            item {
                AppTextField(
                    label = "Password",
                    value = registrationPageState.password,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnPasswordChange(it))},
                    error = registrationPageState.passwordError,
                    placeholder = "*********",
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
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnConfirmPasswordChange(it)) },
                    error = registrationPageState.confirmPasswordError,
                    placeholder = "*********",
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
                registrationStatus.message?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }

                AppButton(
                    onClick = {
                        authenticationViewModel.onAction(LoginAction.OnSignup)

                        /*authenticationViewModel.register(
                            registrationRequestDTO =
                            RegistrationRequestDTO(
                                firstName = firstName,
                                lastName = lastName,
                                phone = phone,
                                email = email.lowercase().trim(),
                                password = password,
                            ),
                        )*/
                    },
                    content = {
                        when (registrationStatus.status) {
                            ResultStatus.INITIAL,
                            ResultStatus.ERROR -> {
                                Text("Log in")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Log in")
                                navigator.replaceAll(MoreDetailsPage())
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
                    TextButton(onClick = { navigator.pop() }) {
                        Text(text = "Sign in", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview(){
    RegistrationScreen()
}
