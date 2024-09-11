package com.charlesmuchogo.research.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class TabRowItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
){
    companion object {
        val testItems = listOf(
            TabRowItem(
                title = "Single test",
                selectedIcon = Icons.Filled.Person,
                unSelectedIcon = Icons.Outlined.Person,
            ),
            TabRowItem(
                title = "Couple test",
                selectedIcon = Icons.Filled.Group,
                unSelectedIcon = Icons.Outlined.Group,
            ),
        )
    }
}
