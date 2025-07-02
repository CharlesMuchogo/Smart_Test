package com.charlesmuchogo.research.presentation.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.states.LoginState
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.navigation.ForgotPasswordPage
import com.charlesmuchogo.research.navigation.HomePage
import com.charlesmuchogo.research.navigation.LoginPage
import com.charlesmuchogo.research.navigation.MoreDetailsPage
import com.charlesmuchogo.research.navigation.RegistrationPage

@Composable
fun LoginRoot() {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val loginPageState = loginViewModel.state.collectAsStateWithLifecycle()
    LoginScreen(
        state = loginPageState.value,
        onAction = loginViewModel::onAction
    )
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, state: LoginState, onAction: (LoginAction) -> Unit) {

    LaunchedEffect(state.hasLoggedIn) {
        if(state.hasLoggedIn){
            navController.navigate(
                route = if (state.loggedInUser?.educationLevel.isNullOrBlank()) MoreDetailsPage else HomePage
            ) {
                popUpTo(LoginPage) {
                    inclusive = true
                }
            }
        }
    }



    Scaffold { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .imePadding()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.password),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier =
                        Modifier
                            .fillParentMaxHeight(0.23f)
                            .padding(vertical = 24.dp),
                    )
                }
                Text(
                    text = "Log in",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = SemiBold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Welcome back! Please enter your details to continue.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Normal),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                AppTextField(
                    label = "Email",
                    value = state.email,
                    placeholder = "johndoe@email.com",
                    error = state.emailError,
                    onValueChanged = { onAction(LoginAction.OnEmailChange(it)) },
                    keyboardType = KeyboardType.Email,
                )
            }

            item {
                AppTextField(
                    label = "Password",
                    value = state.password,
                    placeholder = "*********",
                    error = state.passwordError,
                    onValueChanged = {
                        onAction(
                            LoginAction.OnPasswordChange(
                                it
                            )
                        )
                    },
                    keyboardType = KeyboardType.Password,
                    passwordVisible = state.showPassword,
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(onClick = {
                            onAction(LoginAction.OnShowPasswordChange(!state.showPassword))
                        }) {
                            Icon(
                                imageVector = if (state.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "show password",
                            )
                        }
                    },
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.rememberMe,
                            onCheckedChange = { isChecked ->
                                onAction(
                                    LoginAction.OnRememberMeChange(isChecked)
                                )
                            },
                        )
                        Text(
                            "Remember me",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = SemiBold),
                        )
                    }
                    Text(
                        "Forgot password?",
                        style =
                        MaterialTheme.typography.labelLarge.copy(
                            fontWeight = SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier =
                        Modifier
                            .padding(top = 16.dp)
                            .clickable {
                                navController.navigate(ForgotPasswordPage)
                            },
                    )
                }
            }

            item {
                state.message?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }

                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = {
                        onAction(LoginAction.OnLogin)
                    },
                    content = {
                        when (state.isLoggingIn) {
                            true -> {
                                AppLoginButtonContent(message = "Authenticating...")
                            }
                            false -> {
                                Text("Log in")
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
                    Text(
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(onClick = { navController.navigate(RegistrationPage) }) {
                        Text(text = "Sign up", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
