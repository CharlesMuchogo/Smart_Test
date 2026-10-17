package com.charlesmuchogo.research.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ui.theme.AmberContainerDark
import ui.theme.AmberContainerLight
import ui.theme.AmberDark
import ui.theme.AmberLight
import ui.theme.CoralContainerDark
import ui.theme.CoralContainerLight
import ui.theme.CoralDark
import ui.theme.CoralLight
import ui.theme.OnAmberContainerDark
import ui.theme.OnAmberContainerLight
import ui.theme.OnCoralContainerDark
import ui.theme.OnCoralContainerLight
import ui.theme.OnSuccessContainerDark
import ui.theme.OnSuccessContainerLight
import ui.theme.SuccessContainerDark
import ui.theme.SuccessContainerLight
import ui.theme.SuccessDark
import ui.theme.SuccessLight

@Immutable
data class ExtendedColors(
    val coral: Color,
    val coralContainer: Color,
    val onCoralContainer: Color,
    val amber: Color,
    val amberContainer: Color,
    val onAmberContainer: Color,
    val success: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

val LightExtendedColors = ExtendedColors(
    coral = CoralLight,
    coralContainer = CoralContainerLight,
    onCoralContainer = OnCoralContainerLight,
    amber = AmberLight,
    amberContainer = AmberContainerLight,
    onAmberContainer = OnAmberContainerLight,
    success = SuccessLight,
    successContainer = SuccessContainerLight,
    onSuccessContainer = OnSuccessContainerLight,
)

val DarkExtendedColors = ExtendedColors(
    coral = CoralDark,
    coralContainer = CoralContainerDark,
    onCoralContainer = OnCoralContainerDark,
    amber = AmberDark,
    amberContainer = AmberContainerDark,
    onAmberContainer = OnAmberContainerDark,
    success = SuccessDark,
    successContainer = SuccessContainerDark,
    onSuccessContainer = OnSuccessContainerDark,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current
