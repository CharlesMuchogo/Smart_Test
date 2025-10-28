package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.showInterstitialAd
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ChatPage
import com.charlesmuchogo.research.navigation.SearchClinicsPage
import com.charlesmuchogo.research.presentation.articles.ArticlesScreen
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.homepage.HomeScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen
import com.charlesmuchogo.research.presentation.utils.ALMOST_BLUR_ALPHA
import kotlinx.coroutines.delay


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BottomBarRoot(animatedVisibilityScope: AnimatedVisibilityScope) {
    val viewModel = hiltViewModel<BottomBarViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    BottomBarScreen(
        state = state,
        onAction = viewModel::onAction,
        animatedVisibilityScope= animatedVisibilityScope
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BottomBarScreen(state: BottomBarState, onAction: (BottomBarAction) -> Unit, animatedVisibilityScope: AnimatedVisibilityScope) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.showAd) {
            delay(5_000L)
            showInterstitialAd(context, onShowAd = {
                onAction(BottomBarAction.OnHasShownAd)
            })
        }
    }

    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = R.string.Instructions,
            selectedIcon = Icons.Default.Info,
            unselectedIcon = Icons.Outlined.Info,
            screen = { HomeScreen(animatedVisibilityScope) }
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

    Scaffold(
        topBar = {

            if (state.selectedBottomBarItem != 0) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(bottomNavigationItems[state.selectedBottomBarItem].title),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    actions = {
                        if (state.selectedBottomBarItem == 0) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            onAction(
                                                BottomBarAction.OnUpdateSelectedLanguage(
                                                    !state.selectedKiswahiliLanguage
                                                )
                                            )
                                        }),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "change language",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )


                                when (state.selectedKiswahiliLanguage) {
                                    true -> Text("KISW")
                                    else -> Text("ENG")
                                }

                            }
                        }

                        if (state.selectedBottomBarItem == 1) {
                            IconButton(onClick = {
                                navController.navigate(SearchClinicsPage)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ChatPage)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.offset(y = 72.dp), shape = CircleShape
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Message, contentDescription = "Chat")
            }
        },
        bottomBar = {
            BottomAppBar(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = ALMOST_BLUR_ALPHA),
            ) {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier =
                            Modifier
                                .offset(x = if (item == bottomNavigationItems[1]) -(20).dp else if (item == bottomNavigationItems[2]) 20.dp else 0.dp)
                                .testTag(stringResource(item.title)),
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.background),
                        interactionSource = remember { MutableInteractionSource() },
                        selected = bottomNavigationItems[state.selectedBottomBarItem]  == item,
                        onClick = {
                            onAction(BottomBarAction.OnUpdateSelectedItem(index))
                        },
                        label = {
                            Text(
                                text = stringResource(item.title),
                                color =
                                    if (index == state.selectedBottomBarItem) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    },
                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == state.selectedBottomBarItem) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = stringResource(item.title),
                                tint =
                                    if (index == state.selectedBottomBarItem) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    },
                            )
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            AuthControllerScreen(
                modifier = Modifier,
                screen =  bottomNavigationItems[state.selectedBottomBarItem].screen,
                authRequired = bottomNavigationItems[state.selectedBottomBarItem].authRequired,
            )
        }
    }
}



