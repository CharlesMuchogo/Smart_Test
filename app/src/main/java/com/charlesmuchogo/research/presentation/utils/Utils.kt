package com.charlesmuchogo.research.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
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

const val PERMISSION_REQUEST_CODE = 123

@SuppressLint("InlinedApi")
@Composable
fun RequestPermissions() {
    val context = LocalContext.current as Activity
    ActivityCompat.requestPermissions(
        context,
        arrayOf(
            Manifest.permission.CAMERA
        ),
        PERMISSION_REQUEST_CODE
    )
}

fun decodeExceptionMessage(e: Exception): String {
    e.printStackTrace()
    if (e.message?.lowercase()?.contains("failed to connect") == true) {
        return "Check your Internet connection and try again"
    }
    return "Something went wrong. Try again"
}

private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

fun isValidEmail(email: String): Boolean {
    return email.matches(emailRegex)
}

fun convertMillisecondsToTimeTaken(milliseconds: Long): String {
    val hours = (milliseconds / (1000 * 60 * 60)).toString().padStart(2, '0')
    val minutes =
        ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toString().padStart(2, '0')
    val seconds = ((milliseconds % (1000 * 60)) / 1000).toString().padStart(2, '0')
    return "$hours:$minutes:$seconds"
}
