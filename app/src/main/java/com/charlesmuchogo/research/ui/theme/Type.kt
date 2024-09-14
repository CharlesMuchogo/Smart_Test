package com.charlesmuchogo.research.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.charlesmuchogo.research.R


@Composable
fun ChirpFontFamily() = FontFamily(
    Font(R.font.chirpregular, weight = FontWeight.Normal),
    Font(R.font.chirpmedium, weight = FontWeight.Medium),
    Font(R.font.chirpbold, weight = FontWeight.SemiBold),
)

@Composable
fun ChirpTypography() = Typography().run {

    val fontFamily = ChirpFontFamily()
    copy(
        //displayLarge = displayLarge.copy(fontFamily = fontFamily),
        // displayMedium = displayMedium.copy(fontFamily = fontFamily),
        //displaySmall = displaySmall.copy(fontFamily = fontFamily),
        //headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        //headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        //headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        //titleLarge = titleLarge.copy(fontFamily = fontFamily),
        //titleMedium = titleMedium.copy(fontFamily = fontFamily),
        //titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily =  fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        //labelLarge = labelLarge.copy(fontFamily = fontFamily),
        //labelMedium = labelMedium.copy(fontFamily = fontFamily),
        // labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}