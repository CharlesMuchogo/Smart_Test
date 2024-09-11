package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.bottomBar.HomePage
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.utils.ResultStatus


class LoginPage : Screen {
    @Composable
    override fun Content() {
        LoginScreen(modifier = Modifier)
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    Scaffold { padding ->
        val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
        val loginStatus = authenticationViewModel.loginStatus.collectAsState().value

        val navigator = LocalNavigator.currentOrThrow
        var email by remember { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var rememberMe by remember { mutableStateOf(true) }
        var passwordVisible by remember { mutableStateOf(false) }
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 200.dp, height = 200.dp)
                            .padding(vertical = 8.dp)
                    )
                }
                Text(
                    "Log in to your account",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = SemiBold)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Welcome back! Please enter your details to continue.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Normal)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }


            item {
                AppTextField(
                    label = "Email",
                    value = email,
                    onValueChanged = { email = it },
                    error = null,
                    placeholder = "johndoe@email.com",
                    keyboardType = KeyboardType.Email
                )
            }

            item {
                AppTextField(
                    label = "Password",
                    value = password,
                    onValueChanged = { password = it },
                    error = null,
                    placeholder = "*********",
                    keyboardType = KeyboardType.Password,
                    passwordVisible = passwordVisible,
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "show password"
                            )
                        }
                    }
                )
            }

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { isChecked -> rememberMe = isChecked }
                        )
                        Text(
                            "Remember me",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = SemiBold),
                        )
                    }
                    Text("Forgot password?",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable {
                                navigator.push(ForgotPasswordPage())
                            })

                }
            }

            item {
                loginStatus.message?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }

                AppButton(
                    onClick = {
                       authenticationViewModel.login(
                            loginRequestDTO = LoginRequestDTO(
                                email = email.lowercase().trim(),
                                password = password,
                            ),
                        )


                    },
                    content = {
                        when (loginStatus.status) {
                            ResultStatus.INITIAL,

                            ResultStatus.ERROR -> {
                                Text("Log in")
                            }

                            ResultStatus.SUCCESS -> {
                                Text("Log in")
                                navigator.replaceAll(HomePage())
                            }

                            ResultStatus.LOADING -> {
                                AppLoginButtonContent(message = "Authenticating...")
                            }
                        }


                    }
                )
            }

        }

    }
}









