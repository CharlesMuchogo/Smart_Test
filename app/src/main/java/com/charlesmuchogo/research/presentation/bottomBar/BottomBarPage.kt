package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.DeepLink
import com.charlesmuchogo.research.navigation.DoctorsPage
import com.charlesmuchogo.research.navigation.PendingNavigation
import com.charlesmuchogo.research.navigation.TestPage
import com.charlesmuchogo.research.presentation.articles.ArticlesScreen
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.homepage.HomeScreen
import com.charlesmuchogo.research.presentation.homepage.startTest
import com.charlesmuchogo.research.presentation.messages.MessagesRoot
import com.charlesmuchogo.research.presentation.profile.ProfileScreen

private const val MESSAGES_TAB = 2

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BottomBarRoot(animatedVisibilityScope: AnimatedVisibilityScope) {
    val viewModel = hiltViewModel<BottomBarViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pendingLink by PendingNavigation.target.collectAsStateWithLifecycle()

    LaunchedEffect(pendingLink) {
        val link = pendingLink ?: return@LaunchedEffect
        PendingNavigation.clear()
        when (link) {
            is DeepLink.Tab -> viewModel.onAction(BottomBarAction.OnUpdateSelectedItem(link.index))
            is DeepLink.Screen -> {
                link.tab?.let { viewModel.onAction(BottomBarAction.OnUpdateSelectedItem(it)) }
                if (link.route == TestPage) startTest(state.hasPendingResults) else navController.navigate(link.route) { launchSingleTop = true }
            }
        }
    }

    BottomBarScreen(
        state = state,
        onAction = viewModel::onAction,
        animatedVisibilityScope= animatedVisibilityScope
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BottomBarScreen(state: BottomBarState, onAction: (BottomBarAction) -> Unit, animatedVisibilityScope: AnimatedVisibilityScope) {


    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = R.string.Home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            screen = { HomeScreen() }
        ),

        BottomNavigationItem(
            title = R.string.learn,
            selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
            unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
            screen = { ArticlesScreen(animatedVisibilityScope) }
        ),

        BottomNavigationItem(
            title = R.string.messages,
            selectedIcon = Icons.AutoMirrored.Filled.Chat,
            unselectedIcon = Icons.AutoMirrored.Outlined.Chat,
            authRequired = true,
            screen = { MessagesRoot() }
        ),

        BottomNavigationItem(
            title = R.string.Profile,
            selectedIcon = Icons.Filled.Person,
            authRequired = true,
            unselectedIcon = Icons.Outlined.Person,
            screen = { ProfileScreen() }
        ),
    )

    val onSelect: (Int) -> Unit = { index ->
        if (index != state.selectedBottomBarItem) {
            onAction(BottomBarAction.OnUpdateSelectedItem(index))
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (state.selectedBottomBarItem != 0) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            text = stringResource(bottomNavigationItems[state.selectedBottomBarItem].title),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    },
                    actions = {
                        if (state.selectedBottomBarItem == MESSAGES_TAB) {
                            FilledIconButton(
                                onClick = { navController.navigate(DoctorsPage) },
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(end = 12.dp),
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.new_conversation))
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                items = bottomNavigationItems,
                selectedIndex = state.selectedBottomBarItem,
                unreadMessages = state.unreadMessages,
                onSelect = onSelect,
                onTakeTest = { startTest(state.hasPendingResults) },
            )
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

@Composable
private fun AppBottomBar(
    items: List<BottomNavigationItem>,
    selectedIndex: Int,
    unreadMessages: Int,
    onSelect: (Int) -> Unit,
    onTakeTest: () -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding(),
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                items.take(2).forEachIndexed { index, item ->
                    NavItem(item = item, selected = index == selectedIndex, badge = 0, onClick = { onSelect(index) }, modifier = Modifier.weight(1f))
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    FilledIconButton(
                        onClick = onTakeTest,
                        shape = RoundedCornerShape(20.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .offset(y = (-14).dp)
                            .size(58.dp)
                            .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = MaterialTheme.colorScheme.primary)
                            .testTag("takeTest"),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Science,
                            contentDescription = stringResource(R.string.takeTest),
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }
                items.drop(2).forEachIndexed { offset, item ->
                    val index = offset + 2
                    NavItem(
                        item = item,
                        selected = index == selectedIndex,
                        badge = if (index == MESSAGES_TAB) unreadMessages else 0,
                        onClick = { onSelect(index) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavItem(
    item: BottomNavigationItem,
    selected: Boolean,
    badge: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val title = stringResource(item.title)
    Column(
        modifier = modifier
            .heightIn(min = 52.dp)
            .clip(MaterialTheme.shapes.medium)
            .selectable(selected = selected, role = Role.Tab, onClick = onClick)
            .testTag(title),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BadgedBox(
            badge = {
                if (badge > 0) {
                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                        Text(if (badge > 99) "99+" else badge.toString())
                    }
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 14.dp, vertical = 3.dp),
            ) {
                Icon(
                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Text(
            text = title,
            color = color,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium),
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
