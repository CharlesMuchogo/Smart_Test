package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen

@Composable
fun TestAuthBlocker() {

    AuthControllerScreen(
        showTopBar = true,
        authRequired = true
    ){
        TestScreen()
    }

}