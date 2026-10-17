package com.charlesmuchogo.research.presentation.messages

import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ChatPage
import com.charlesmuchogo.research.navigation.CommunityPage
import com.charlesmuchogo.research.navigation.ConversationPage
import com.charlesmuchogo.research.navigation.DoctorsPage
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.Pill
import com.charlesmuchogo.research.presentation.messages.components.SegmentedTabs
import com.charlesmuchogo.research.presentation.messages.components.VerifiedBadge
import com.charlesmuchogo.research.presentation.messages.components.conversationColors
import com.charlesmuchogo.research.presentation.messages.components.inboxTime
import com.charlesmuchogo.research.ui.theme.extendedColors

@Composable
fun MessagesRoot() {
    val viewModel = hiltViewModel<MessagesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    MessagesScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(state: MessagesState, onAction: (MessagesAction) -> Unit) {
    LaunchedEffect(Unit) { onAction(MessagesAction.Refresh) }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(MessagesAction.Refresh) },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SegmentedTabs(
                    labels = listOf(
                        stringResource(R.string.filter_all),
                        stringResource(R.string.filter_doctors),
                        stringResource(R.string.community),
                    ),
                    selectedIndex = state.filter.ordinal,
                    onSelect = { onAction(MessagesAction.OnFilterChange(InboxFilter.entries[it])) },
                )
            }

            item { AssistantRow(onClick = { navController.navigate(ChatPage) }) }

            when {
                state.isLoading -> item { AppListLoading(modifier = Modifier.height(240.dp)) }

                state.visibleConversations.isEmpty() -> item { EmptyInbox(error = state.error, onRetry = { onAction(MessagesAction.Refresh) }) }

                else -> item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large),
                    ) {
                        state.visibleConversations.forEachIndexed { index, conversation ->
                            ConversationRow(
                                conversation = conversation,
                                onClick = { navController.navigate(ConversationPage(conversation.id)) },
                            )
                            if (index < state.visibleConversations.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = stringResource(R.string.chats_private_note),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistantRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.extendedColors.amberContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.extendedColors.onAmberContainer,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = stringResource(R.string.smart_assistant),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = stringResource(R.string.assistant_preview),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Pill(
            text = stringResource(R.string.ai_label),
            container = MaterialTheme.extendedColors.amberContainer,
            content = MaterialTheme.extendedColors.onAmberContainer,
        )
    }
}

@Composable
private fun ConversationRow(conversation: Conversation, onClick: () -> Unit) {
    val isCircle = conversation.type == ConversationType.CIRCLE
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(
            name = conversation.title,
            colors = conversationColors(conversation.type),
            shape = if (isCircle) MaterialTheme.shapes.medium else RoundedCornerShape(50),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                if (conversation.verified) {
                    Spacer(Modifier.width(4.dp))
                    VerifiedBadge()
                }
                if (conversation.type != ConversationType.DOCTOR) {
                    Spacer(Modifier.width(6.dp))
                    Pill(
                        text = stringResource(if (isCircle) R.string.circle_tag else R.string.peer_tag),
                        container = MaterialTheme.extendedColors.coralContainer,
                        content = MaterialTheme.extendedColors.onCoralContainer,
                    )
                }
            }
            val preview = conversation.lastMessage.ifBlank { conversation.subtitle }
            if (preview.isNotBlank()) {
                Text(
                    text = preview,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (conversation.unread > 0) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                    color = if (conversation.unread > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = inboxTime(conversation.lastMessageAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (conversation.unread > 0) {
                Box(
                    modifier = Modifier
                        .heightIn(min = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (conversation.unread > 99) "99+" else conversation.unread.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyInbox(error: String?, onRetry: () -> Unit) {
    if (error != null) {
        AppErrorScreen(errorDescription = error, onRetry = onRetry)
        return
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.no_conversations),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.no_conversations_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { navController.navigate(DoctorsPage) }) {
                Text(stringResource(R.string.talk_to_doctor))
            }
            OutlinedButton(onClick = { navController.navigate(CommunityPage) }) {
                Text(stringResource(R.string.community))
            }
        }
    }
}
