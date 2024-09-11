package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppTextField

class ForgotPasswordPage : Screen {
    @Composable
    override fun Content() {
        ForgotPasswordScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    var email by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                    }
                },
                title = { Text("Forgot Password?") }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = modifier.padding(paddingValues).padding(horizontal = 8.dp)) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 200.dp, height = 200.dp).padding(vertical = 20.dp)
                    )
                }
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

                AppButton(
                    onClick = {

                    },
                    content = {
                        Text("Reset password")
                    }
                )
            }
        }
    }
}