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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.charlesmuchogo.research.R

data class BottomNavigationItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
){
    companion object{
        val bottomNavigationItems = listOf(
            BottomNavigationItem(
                title = R.string.Instructions,
                selectedIcon = Icons.Default.Info,
                unselectedIcon = Icons.Outlined.Info,
            ),

            BottomNavigationItem(
                title = R.string.Clinics,
                selectedIcon = Icons.Filled.MedicalServices,
                unselectedIcon = Icons.Outlined.MedicalServices,
            ),

            BottomNavigationItem(
                title = R.string.History,
                selectedIcon = Icons.Filled.Schedule,
                unselectedIcon = Icons.Outlined.Schedule,
            ),

            BottomNavigationItem(
                title = R.string.Profile,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
            ),
        )
    }
}
