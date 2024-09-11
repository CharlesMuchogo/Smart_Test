package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator

class HomePage : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(
            tab = BottomNavigationTabs.InstructionsTab
        ) {
            val tabNavigator = LocalTabNavigator.current
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(title = {
                        Text(
                            tabNavigator.current.options.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    })
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        TabNavigationItem(BottomNavigationTabs.InstructionsTab)
                        TabNavigationItem(BottomNavigationTabs.HistoryTab)
                        TabNavigationItem(BottomNavigationTabs.ProfileTab)
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CurrentScreen()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = { tabNavigator.current = tab }
        ) {
            Icon(
                painter = if (isSelected) {
                    bottomBarTabFilledIcon(tab)
                } else {
                    tab.options.icon!!
                },
                contentDescription = tab.options.title,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }

}