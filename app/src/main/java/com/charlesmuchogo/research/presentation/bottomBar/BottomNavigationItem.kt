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
import com.charlesmuchogo.research.presentation.instructions.InstructionsScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen

data class BottomNavigationItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val screen: @Composable() () -> Unit,
    val authRequired: Boolean = false,
    val unselectedIcon: ImageVector,
){
    companion object{
        val bottomNavigationItems = listOf(
            BottomNavigationItem(
                title = R.string.Instructions,
                selectedIcon = Icons.Default.Info,
                unselectedIcon = Icons.Outlined.Info,
                screen = { InstructionsScreen() }
            ),

            BottomNavigationItem(
                title = R.string.Clinics,
                selectedIcon = Icons.Filled.MedicalServices,
                unselectedIcon = Icons.Outlined.MedicalServices,
                authRequired = true,
                screen = { ClinicsScreen() }
            ),

            BottomNavigationItem(
                title = R.string.History,
                selectedIcon = Icons.Filled.Schedule,
                authRequired = true,
                unselectedIcon = Icons.Outlined.Schedule,
                screen = { HistoryScreen() }
            ),

            BottomNavigationItem(
                title = R.string.Profile,
                selectedIcon = Icons.Filled.Person,
                authRequired = true,
                unselectedIcon = Icons.Outlined.Person,
                screen = { ProfileScreen() }
            ),
        )
    }
}
