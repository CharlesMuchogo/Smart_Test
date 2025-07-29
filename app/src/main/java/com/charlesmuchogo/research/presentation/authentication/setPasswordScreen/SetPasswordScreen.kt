package com.charlesmuchogo.research.presentation.authentication.setPasswordScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.HomePage
import com.charlesmuchogo.research.navigation.LoginPage
import com.charlesmuchogo.research.navigation.SetPasswordPage
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoadingButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField

@Composable
fun SetPasswordRoot(token: String) {

    val viewModel = hiltViewModel<SetPasswordViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        viewModel.onAction(SetPasswordAction.OnUpdateToken(token))
    }

    LaunchedEffect(state.hasUpdatedPassword) {
        if(state.hasUpdatedPassword){
            navController.navigate(HomePage){
                popUpTo<SetPasswordPage> {
                    inclusive = true
                }
            }
        }
    }

    SetPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordScreen(
    state: SetPasswordState,
    onAction: (SetPasswordAction) -> Unit
) {

    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {},
            )
        },
    ) {
        Column(
            modifier = Modifier
                .onGloballyPositioned {
                    focusRequester.requestFocus()
                }
                .padding(it)
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .consumeWindowInsets(it)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.secure_password),
                modifier = Modifier.fillMaxHeight(0.1f),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
            Text(
                "Let's secure your account",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                "Your password should be secure, but don’t forget to make it memorable!",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
            AppTextField(
                textFieldModifier = Modifier.focusRequester(focusRequester),
                label = "Password",
                keyboardType = KeyboardType.Password,
                value = state.password,
                placeholder = "**********",
                passwordVisible = state.showPassword,
                error = state.passwordError,
                onValueChanged = { password ->
                    onAction(
                        SetPasswordAction.OnUpdatePassword(password)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(
                                SetPasswordAction.OnShowPassword(!state.showPassword)
                            )
                        },
                    ) {
                        Icon(
                            imageVector = if (state.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                        )
                    }
                }
            )

            AppTextField(
                label = "Confirm Password",
                keyboardType = KeyboardType.Password,
                value = state.confirmPassword,
                placeholder = "**********",
                passwordVisible = state.showPassword,
                error = state.confirmPasswordError,
                onValueChanged = { password ->
                    onAction(
                        SetPasswordAction.OnUpdateConfirmPassword(password)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(
                                SetPasswordAction.OnShowPassword(!state.showPassword)
                            )
                        },
                    ) {
                        Icon(
                            imageVector = if (state.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                        )
                    }
                }
            )

            Spacer(Modifier.weight(1f))

            AppButton(
                enabled = state.password.isNotBlank() && state.confirmPassword.isNotBlank(),
                shape = MaterialTheme.shapes.extraLarge,
                onClick = {
                    onAction(
                         SetPasswordAction.OnSubmit
                    )
                }
            ) {
                when (state.isSubmittingPassword) {
                    true -> {
                        AppLoadingButtonContent(message = "Setting password...")
                    }

                    false -> {
                        Text("Set password")
                    }
                }
            }
        }
    }
}