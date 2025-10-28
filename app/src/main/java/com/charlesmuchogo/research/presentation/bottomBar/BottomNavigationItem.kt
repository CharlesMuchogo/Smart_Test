package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.instructions.InstructionsRoot
import com.charlesmuchogo.research.presentation.instructions.InstructionsScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen

data class BottomNavigationItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val screen: @Composable() () -> Unit,
    val authRequired: Boolean = false,
    val unselectedIcon: ImageVector,
)
