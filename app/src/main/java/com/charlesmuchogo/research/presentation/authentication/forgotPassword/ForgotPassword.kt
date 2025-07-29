package com.charlesmuchogo.research.presentation.authentication.forgotPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppLoadingButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.common.NavigationIcon

@Composable
fun ForgotPasswordRoot() {

    val viewmodel = hiltViewModel<ForgotPasswordViewmodel>()

    val state by viewmodel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = viewmodel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    NavigationIcon()
                },
                title = { Text(text = stringResource(R.string.forgot_password), style = MaterialTheme.typography.titleMedium) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .consumeWindowInsets(paddingValues)
                .imePadding()
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
                        modifier =
                            Modifier
                                .size(width = 200.dp, height = 200.dp)
                                .padding(vertical = 20.dp),
                    )
                }
            }

            item {
                AppTextField(
                    label = "Email",
                    value = state.email,
                    placeholder = "johndoe@email.com",
                    error = state.emailError,
                    onValueChanged = { onAction(ForgotPasswordAction.OnUpdateEmail(it)) },
                    keyboardType = KeyboardType.Email,
                )

                AppButton(
                    onClick = {
                        onAction(ForgotPasswordAction.OnSubmit)
                    },
                    content = {
                        when(state.isSubmittingEmail){
                            true -> {
                               AppLoadingButtonContent(stringResource(R.string.Loading))
                            }
                            false -> {
                                Text(stringResource(R.string.ResetPassword))
                            }
                        }
                    },
                )
            }
        }
    }
}
