package com.charlesmuchogo.research.presentation.homepage

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.presentation.articles.ArticlesScreen
import com.charlesmuchogo.research.presentation.instructions.InstructionsRoot
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.HomeScreen(animatedVisibilityScope: AnimatedVisibilityScope) {

    val homePageViewModel = hiltViewModel<HomepageViewModel>()
    val currentTab = homePageViewModel.selectedTab.collectAsStateWithLifecycle().value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = currentTab,
            ) {
                HomepageTabs.entries.forEachIndexed { index, item ->
                    Tab(
                        selected = index == currentTab,
                        onClick = {
                            homePageViewModel.updateTab(index)
                        },
                        text = {
                            Text(
                                item.name.lowercase().capitalize(Locale.ROOT),
                                style =
                                    MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (currentTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                                    ),
                            )
                        },
                    )
                }
            }

            when (currentTab) {
                0 -> {
                    InstructionsRoot()
                }

                1 -> {
                    ArticlesScreen(animatedVisibilityScope)
                }
            }
        }
    }
}