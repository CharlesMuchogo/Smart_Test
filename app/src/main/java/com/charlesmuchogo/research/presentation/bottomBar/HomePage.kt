package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.instructions.InstructionsScreen
import com.charlesmuchogo.research.presentation.navigation.SearchClinicsPage
import com.charlesmuchogo.research.presentation.navigation.TestPage
import com.charlesmuchogo.research.presentation.profile.ProfileScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var kiswahiliLanguage by remember { mutableStateOf(true) }

    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()

    LaunchedEffect(key1 = true) {
        testResultsViewModel.fetchTestResults()
    }


    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                FloatingActionButton(
                    modifier = Modifier
                        .offset(y = 72.dp, x = 16.dp)
                        .size(52.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        navController.navigate(TestPage)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                    ),
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Task",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                BottomNavigationItem.bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        interactionSource = remember { MutableInteractionSource() },
                        modifier = Modifier
                            .testTag(item.title)
                            .offset(
                                x = when (index) {
                                    0 -> 0.dp
                                    1 -> (-24).dp
                                    2 -> 24.dp
                                    3 -> 0.dp
                                    else -> 0.dp
                                },
                            ),
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (selectedItemIndex == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onBackground

                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (selectedItemIndex == index) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            )
                        }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        BottomNavigationItem.bottomNavigationItems[selectedItemIndex].title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                actions = {
                    if (selectedItemIndex == 0) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { kiswahiliLanguage = !kiswahiliLanguage }),

                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "change language",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )


                            when (kiswahiliLanguage) {
                                true -> Text("KISW")
                                else -> Text("ENG")
                            }

                        }
                    }
                    if (selectedItemIndex == 1) {
                        IconButton(onClick = {
                            navController.navigate(SearchClinicsPage)
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }


            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

            when (selectedItemIndex) {
                0 -> {
                    InstructionsScreen(
                        navController = navController,
                        isKiswahiliLanguage = kiswahiliLanguage
                    )
                }

                1 -> {
                    ClinicsScreen()
                }

                2 -> {
                    HistoryScreen(navController = navController)
                }

                3 -> {
                    ProfileScreen(navController = navController)
                }

            }
        }

    }
}



