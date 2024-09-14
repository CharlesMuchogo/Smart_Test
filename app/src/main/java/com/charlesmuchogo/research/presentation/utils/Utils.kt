package com.charlesmuchogo.research.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

val LocalAppNavigator: ProvidableCompositionLocal<Navigator?> = staticCompositionLocalOf { null }

@Composable
fun ProvideAppNavigator(
    navigator: Navigator,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalAppNavigator provides navigator) {
        content()
    }
}

fun decodeExceptionMessage(e: Exception): String {
    e.printStackTrace()
    if (e.message?.lowercase()?.contains("failed to connect") == true) {
        return "Check your Internet connection and try again"
    }
    return "Something went wrong. Try again"
}
