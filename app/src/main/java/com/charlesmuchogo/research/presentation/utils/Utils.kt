package com.charlesmuchogo.research.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.charlesmuchogo.research.data.network.httpUrlBuilder
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CompletableDeferred
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import java.util.Locale


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

private val dateRegex = "^\\d{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[01])$".toRegex()

fun isValidDate(date: String): Boolean {
    return date.matches(dateRegex)
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

fun openInAppBrowser(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setUrlBarHidingEnabled(true)
        .build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

fun convertTimestampToDate(timeStamp: Long): String {
    val isMilliseconds = timeStamp > 15_000_000_000L
    val instant =
        if (isMilliseconds) {
            Instant.fromEpochMilliseconds(timeStamp)
        } else {
            Instant.fromEpochSeconds(timeStamp)
        }
    val timeZone = TimeZone.currentSystemDefault()
    val localDateTime = instant.toLocalDateTime(timeZone)
    val formattedDate = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val formattedMonth = localDateTime.monthNumber.toString().padStart(2, '0')
    val formattedYear = localDateTime.year.toString().padStart(2, '0')
    return "$formattedYear-$formattedMonth-$formattedDate"
}

fun calculateDifferenceBetweenDates(
    startDate: String,
    endDate: String,
): Long  {
    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)

    return start.daysUntil(end).toLong()
}


val levelsOfEducation = listOf(
    "Primary School",
    "High School",
    "Diploma",
    "Bachelor's Degree",
    "Master's Degree",
    "Doctorate (PhD)",
    "Postdoctoral"
)

val genders = listOf(
    "Female",
    "Male",
    "Non-Binary",
    "Prefer not to say"
)

fun setAppLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocales(LocaleList(locale))

    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

fun getDeviceCountry(context: Context): String {
    val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val simCountry = telephonyManager.simCountryIso
    val networkCountry = telephonyManager.networkCountryIso

    return if (simCountry.isNotEmpty()) {
        simCountry.uppercase(Locale.US)
    } else if (networkCountry.isNotEmpty()) {
        networkCountry.uppercase(Locale.US)
    } else {
        Locale.getDefault().country
    }
}

suspend fun getFcmToken(): String {
    val deferred = CompletableDeferred<String>()
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result
                Log.d("FCM Token", "FcmToken: $fcmToken")
                deferred.complete(fcmToken ?: "")
            } else {
                Log.e("FCM Token", "Failed to get token: ${task.exception}")
                deferred.complete("")
            }
        }
    return deferred.await()
}

fun formatChatDate(date: LocalDate): String {
    return "${
        date.dayOfMonth.toString().padStart(2, '0')
    } ${date.month.name.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} ${date.year}"
}

fun convertTimestampToTime(timeStamp: Long): String {
    val isMilliseconds = timeStamp > 15_000_000_000L
    val instant =
        if (isMilliseconds) {
            Instant.fromEpochMilliseconds(timeStamp)
        } else {
            Instant.fromEpochSeconds(timeStamp)
        }
    val timeZone = TimeZone.currentSystemDefault()
    val localDateTime = instant.toLocalDateTime(timeZone)

    val formattedHour = localDateTime.hour.toString().padStart(2, '0')
    val formattedMinute = localDateTime.minute.toString().padStart(2, '0')
    return "$formattedHour:$formattedMinute"
}

@Composable
fun Modifier.removeRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}

const val ALMOST_BLUR_ALPHA = 0.95f
val PRIVACY_POLICY_URL = httpUrlBuilder() + "/privacy_policy"
val TERMS_AND_CONDITIONS_URL = httpUrlBuilder() + "/terms_and_conditions"
