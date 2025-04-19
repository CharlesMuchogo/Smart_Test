package com.charlesmuchogo.research.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.charlesmuchogo.research.R

data class TabRowItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {
    companion object {
        val testItems =
            listOf(
                TabRowItem(
                    title = R.string.individualTest,
                    selectedIcon = Icons.Filled.Person,
                    unSelectedIcon = Icons.Outlined.Person,
                ),
                TabRowItem(
                    title = R.string.couplesTest,
                    selectedIcon = Icons.Filled.Group,
                    unSelectedIcon = Icons.Outlined.Group,
                ),
            )
    }
}
