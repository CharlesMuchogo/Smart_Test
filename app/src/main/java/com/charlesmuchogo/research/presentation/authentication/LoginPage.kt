package com.charlesmuchogo.research.presentation.authentication

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.navigation.ForgotPasswordPage
import com.charlesmuchogo.research.presentation.navigation.HomePage
import com.charlesmuchogo.research.presentation.navigation.LoginPage
import com.charlesmuchogo.research.presentation.navigation.MoreDetailsPage
import com.charlesmuchogo.research.presentation.navigation.RegistrationPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val loginStatus = authenticationViewModel.loginStatus.collectAsState().value
    val loginPageState = authenticationViewModel.loginPageState



    Scaffold { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .imePadding()
                .padding(padding)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.login),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier =
                        Modifier
                            .fillParentMaxHeight(0.3f)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(vertical = 24.dp),
                    )
                }
                Text(
                    text = "Log in to your account",
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
                    label = "Email or phone number",
                    value = loginPageState.email,
                    onValueChanged = { authenticationViewModel.onAction(LoginAction.OnEmailChange(it)) },
                    error = loginPageState.emailError,
                    placeholder = "johndoe@email.com or 0712345678",
                    keyboardType = KeyboardType.Email,
                )
            }

            item {
                AppTextField(
                    label = "Password",
                    value = loginPageState.password,
                    onValueChanged = {
                        authenticationViewModel.onAction(
                            LoginAction.OnPasswordChange(
                                it
                            )
                        )
                    },
                    error = loginPageState.passwordError,
                    placeholder = "*********",
                    keyboardType = KeyboardType.Password,
                    passwordVisible = loginPageState.showPassword,
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(onClick = {
                            authenticationViewModel.onAction(LoginAction.OnShowPasswordChange(!loginPageState.showPassword))
                        }) {
                            Icon(
                                imageVector = if (loginPageState.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
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
                            checked = loginPageState.rememberMe,
                            onCheckedChange = { isChecked ->
                                authenticationViewModel.onAction(
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
                loginStatus.message?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }

                AppButton(
                    onClick = {
                        authenticationViewModel.onAction(LoginAction.OnLogin)
                    },
                    content = {
                        when (loginStatus.status) {
                            ResultStatus.INITIAL, ResultStatus.ERROR -> {
                                Text("Log in")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Log in")
                                loginStatus.data?.let { response ->
                                    navController.navigate(
                                        route = if (response.user.educationLevel.isBlank() || response.user.age.isBlank()) MoreDetailsPage else HomePage
                                    ) {
                                        popUpTo(LoginPage) {
                                            inclusive = true
                                        }
                                    }
                                }

                            }

                            ResultStatus.LOADING -> {
                                AppLoginButtonContent(message = "Authenticating...")
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
