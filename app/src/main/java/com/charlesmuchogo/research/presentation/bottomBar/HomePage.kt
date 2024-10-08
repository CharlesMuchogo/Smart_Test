package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.authentication.LoginPage
import com.charlesmuchogo.research.presentation.testpage.TestPage
import com.charlesmuchogo.research.presentation.utils.LocalAppNavigator

class HomePage : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
        val testResultsViewModel = hiltViewModel<TestResultsViewModel>()
        val authenticationEventState = authenticationViewModel.authenticationEventState.collectAsStateWithLifecycle().value
        val navigator = LocalAppNavigator.currentOrThrow

        val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

        LaunchedEffect(authenticationEventState.status) {
            authenticationEventState.data?.let {
                if (it.logout) {
                    authenticationViewModel.logout()
                    navigator.replaceAll(LoginPage())
                }
            }
        }

        LaunchedEffect(key1 = true) {
            testResultsViewModel.fetchTestResults()
        }
        TabNavigator(
            tab = BottomNavigationTabs.InstructionsTab,
        ) {
            val tabNavigator = LocalTabNavigator.current
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(title = {
                        Text(
                            tabNavigator.current.options.title,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    })
                },
                floatingActionButton = {
                    if (tabNavigator.current != BottomNavigationTabs.ProfileTab) {
                        ExtendedFloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                navigator.push(TestPage())
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Take a test"
                                )
                            },
                            text = { Text(text = "Take a test") },
                        )
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        scrollBehavior = scrollBehavior,
                        containerColor = MaterialTheme.colorScheme.background,
                    ) {
                        TabNavigationItem(BottomNavigationTabs.InstructionsTab)
                        TabNavigationItem(BottomNavigationTabs.ClinicsTab)
                        TabNavigationItem(BottomNavigationTabs.HistoryTab)
                        TabNavigationItem(BottomNavigationTabs.ProfileTab)
                    }
                },
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

    val color =   if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Column(modifier = Modifier
        .weight(1f)
        .clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = { tabNavigator.current = tab }
        ), horizontalAlignment = Alignment.CenterHorizontally){
        Icon(
            painter =
            if (isSelected) {
                bottomBarTabFilledIcon(tab)
            } else {
                tab.options.icon!!
            },
            contentDescription = tab.options.title,
            tint = color,
        )
        Text(modifier = Modifier.padding(top = 4.dp), text = tab.options.title, style = MaterialTheme.typography.labelLarge.copy(color = color, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium ))
    }
}
