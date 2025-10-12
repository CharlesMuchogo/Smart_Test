package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
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
import com.charlesmuchogo.research.ads.showInterstitialAd
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ChatPage
import com.charlesmuchogo.research.navigation.SearchClinicsPage
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.utils.ALMOST_BLUR_ALPHA
import kotlinx.coroutines.delay


@Composable
fun BottomBarRoot() {
    val viewModel = hiltViewModel<BottomBarViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(state: BottomBarState, onAction: (BottomBarAction) -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.showAd) {
            delay(5_000L)
            showInterstitialAd(context, onShowAd = {
                onAction(BottomBarAction.OnHasShownAd)
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(state.selectedBottomBarItem.title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                actions = {
                    if (state.selectedBottomBarItem == BottomNavigationItem.bottomNavigationItems.first()) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onAction(BottomBarAction.OnUpdateSelectedLanguage(!state.selectedKiswahiliLanguage)) }),

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

                    if (state.selectedBottomBarItem == BottomNavigationItem.bottomNavigationItems.first()) {
                        IconButton(onClick = {
                            navController.navigate(SearchClinicsPage)
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            )
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
                BottomNavigationItem.bottomNavigationItems.forEach { item ->
                    NavigationBarItem(
                        modifier =
                            Modifier
                                .offset(x = if (item == BottomNavigationItem.bottomNavigationItems[1]) -(20).dp else if (item == BottomNavigationItem.bottomNavigationItems[2]) 20.dp else 0.dp)
                                .testTag(stringResource(item.title)),
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.background),
                        interactionSource = remember { MutableInteractionSource() },
                        selected = state.selectedBottomBarItem == item,
                        onClick = {
                            onAction(BottomBarAction.OnUpdateSelectedItem(item))
                        },
                        label = {
                            Text(
                                text = stringResource(item.title),
                                color =
                                    if (item == state.selectedBottomBarItem) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    },
                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (item == state.selectedBottomBarItem) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = stringResource(item.title),
                                tint =
                                    if (item == state.selectedBottomBarItem) {
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
                screen = state.selectedBottomBarItem.screen,
                authRequired = state.selectedBottomBarItem.authRequired,
            )
        }
    }
}



