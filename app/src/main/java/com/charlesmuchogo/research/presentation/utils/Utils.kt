package com.charlesmuchogo.research.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat



const val PERMISSION_REQUEST_CODE = 123

@SuppressLint("InlinedApi")
@Composable
fun RequestPermissions() {
    val context = LocalContext.current as Activity
    ActivityCompat.requestPermissions(
        context,
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS
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

fun openDialer(phoneNumber: String, context: Context) {
    try {
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.applicationContext.startActivity(dialIntent)
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}
