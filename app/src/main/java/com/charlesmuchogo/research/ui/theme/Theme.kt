package com.charlesmuchogo.research.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ui.theme.BackgroundDark
import ui.theme.BackgroundLight
import ui.theme.ErrorContainerDark
import ui.theme.ErrorContainerLight
import ui.theme.ErrorDark
import ui.theme.ErrorLight
import ui.theme.GreenContainerDark
import ui.theme.GreenContainerLight
import ui.theme.GreenPrimaryDark
import ui.theme.GreenPrimaryLight
import ui.theme.GreenSecondaryContainerDark
import ui.theme.GreenSecondaryContainerLight
import ui.theme.GreenSecondaryDark
import ui.theme.GreenSecondaryLight
import ui.theme.GreenTertiaryContainerDark
import ui.theme.GreenTertiaryContainerLight
import ui.theme.GreenTertiaryDark
import ui.theme.GreenTertiaryLight
import ui.theme.OnBackgroundDark
import ui.theme.OnBackgroundLight
import ui.theme.OnErrorDark
import ui.theme.OnErrorLight
import ui.theme.OnGreenContainerDark
import ui.theme.OnGreenContainerLight
import ui.theme.OnGreenDark
import ui.theme.OnGreenLight
import ui.theme.OnGreenSecondaryContainerDark
import ui.theme.OnGreenSecondaryContainerLight
import ui.theme.OnGreenSecondaryDark
import ui.theme.OnGreenSecondaryLight
import ui.theme.OnGreenTertiaryContainerDark
import ui.theme.OnGreenTertiaryContainerLight
import ui.theme.OnGreenTertiaryDark
import ui.theme.OnGreenTertiaryLight
import ui.theme.OnSurfaceDark
import ui.theme.OnSurfaceLight
import ui.theme.OnSurfaceVariantDark
import ui.theme.OnSurfaceVariantLight
import ui.theme.OutlineDark
import ui.theme.OutlineLight
import ui.theme.SurfaceDark
import ui.theme.SurfaceLight
import ui.theme.SurfaceVariantDark
import ui.theme.SurfaceVariantLight
import ui.theme.lightGray

val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    secondary = GreenSecondaryDark,
    tertiary = GreenTertiaryDark,
    onPrimary = OnGreenDark,
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,
    onSecondary = OnGreenSecondaryDark,
    secondaryContainer = GreenSecondaryContainerDark,
    onSecondaryContainer = OnGreenSecondaryContainerDark,
    onTertiary = OnGreenTertiaryDark,
    onTertiaryContainer = OnGreenTertiaryContainerDark,
    tertiaryContainer = GreenTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = lightGray,
    outline = OutlineDark,
)

val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryLight,
    secondary = GreenSecondaryLight,
    tertiary = GreenTertiaryLight,
    onPrimary = OnGreenLight,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,
    onSecondary = OnGreenSecondaryLight,
    secondaryContainer = GreenSecondaryContainerLight,
    onSecondaryContainer = OnGreenSecondaryContainerLight,
    onTertiary = OnGreenTertiaryLight,
    onTertiaryContainer = OnGreenTertiaryContainerLight,
    tertiaryContainer = GreenTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = lightGray,
    outline = OutlineLight,
)

@Composable
fun SmartTestTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(
                window,
                view
            ).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChirpTypography(),
        content = content
    )
}