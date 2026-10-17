package com.charlesmuchogo.research.presentation.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.chat.BlockedUser
import com.charlesmuchogo.research.domain.models.chat.ChatRole
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.presentation.messages.components.conversationColors
import com.charlesmuchogo.research.presentation.messages.components.dayLabel
import com.charlesmuchogo.research.presentation.messages.components.localDate

@Composable
fun BlockedUsersRoot() {
    val viewModel = hiltViewModel<BlockedUsersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthControllerScreen(showTopBar = true) {
        BlockedUsersScreen(state = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedUsersScreen(state: BlockedUsersState, onAction: (BlockedUsersAction) -> Unit) {
    state.unblockTarget?.let { user ->
        AlertDialog(
            onDismissRequest = { onAction(BlockedUsersAction.OnUnblockRequest(null)) },
            title = { Text(stringResource(R.string.unblock_title, user.name)) },
            text = { Text(stringResource(R.string.unblock_body)) },
            confirmButton = {
                TextButton(onClick = { onAction(BlockedUsersAction.OnConfirmUnblock) }) {
                    Text(stringResource(R.string.unblock))
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(BlockedUsersAction.OnUnblockRequest(null)) }) {
                    Text(stringResource(R.string.Cancel))
                }
            },
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.blocked_people),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    )
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading && state.users.isEmpty() -> AppListLoading(modifier = Modifier.padding(padding))

            state.error != null && state.users.isEmpty() -> AppErrorScreen(
                modifier = Modifier.padding(padding),
                errorDescription = state.error,
                onRetry = { onAction(BlockedUsersAction.Load) },
            )

            state.users.isEmpty() -> EmptyBlockedList(modifier = Modifier.padding(padding))

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    Text(
                        text = stringResource(R.string.blocked_people_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }
                items(state.users, key = { it.userId }) { user ->
                    BlockedUserRow(
                        user = user,
                        isUnblocking = state.unblockingId == user.userId,
                        onUnblock = { onAction(BlockedUsersAction.OnUnblockRequest(user)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BlockedUserRow(user: BlockedUser, isUnblocking: Boolean, onUnblock: () -> Unit) {
    val type = if (user.role == ChatRole.DOCTOR) ConversationType.DOCTOR else ConversationType.PEER
    val blockedOn = localDate(user.blockedAt)
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Avatar(name = user.name, colors = conversationColors(type), size = 44.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                if (blockedOn != null) {
                    Text(
                        text = stringResource(R.string.blocked_on, dayLabel(blockedOn)),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (isUnblocking) {
                CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
            } else {
                OutlinedButton(onClick = onUnblock, shape = MaterialTheme.shapes.small) {
                    Text(stringResource(R.string.unblock), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmptyBlockedList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = Icons.Outlined.Block,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = stringResource(R.string.no_blocked_people),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.no_blocked_people_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
