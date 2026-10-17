package com.charlesmuchogo.research.presentation.messages

import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.models.chat.ChatMessage
import com.charlesmuchogo.research.domain.models.chat.ChatParticipant
import com.charlesmuchogo.research.domain.models.chat.ChatRole
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.domain.models.chat.MessageKind
import com.charlesmuchogo.research.domain.models.chat.SharedResult
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ConversationPage
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.InfoBanner
import com.charlesmuchogo.research.presentation.messages.components.VerifiedBadge
import com.charlesmuchogo.research.presentation.messages.components.clockTime
import com.charlesmuchogo.research.presentation.messages.components.conversationColors
import com.charlesmuchogo.research.presentation.messages.components.dayLabel
import com.charlesmuchogo.research.presentation.messages.components.localDate
import com.charlesmuchogo.research.ui.theme.extendedColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.LocalDate

@Composable
fun ConversationRoot() {
    val viewModel = hiltViewModel<ConversationViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ConversationEvent.Close -> navController.popBackStack()
                is ConversationEvent.OpenConversation -> navController.navigate(ConversationPage(event.id))
            }
        }
    }

    AuthControllerScreen(showTopBar = true) {
        ConversationScreen(state = state, onAction = viewModel::onAction)
    }
}

private sealed interface ChatListItem {
    val key: String

    data class MessageItem(val message: ChatMessage, val showSender: Boolean) : ChatListItem {
        override val key: String = if (message.id > 0) "m${message.id}" else "c${message.clientId}"
    }

    data class DateItem(val date: LocalDate) : ChatListItem {
        override val key: String = "d$date"
    }
}

private fun buildItems(messages: List<ChatMessage>, myUserId: Long?, isCircle: Boolean): List<ChatListItem> {
    val items = mutableListOf<ChatListItem>()
    val newestFirst = messages.asReversed()
    newestFirst.forEachIndexed { index, message ->
        val older = newestFirst.getOrNull(index + 1)
        val showSender = isCircle && message.senderId != myUserId && older?.senderId != message.senderId
        items += ChatListItem.MessageItem(message, showSender)
        val date = localDate(message.createdAt)
        val olderDate = older?.let { localDate(it.createdAt) }
        if (date != null && date != olderDate) items += ChatListItem.DateItem(date)
    }
    return items
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(state: ConversationState, onAction: (ConversationAction) -> Unit) {
    val listState = rememberLazyListState()
    val isCircle = state.type == ConversationType.CIRCLE
    val items = remember(state.messages, state.myUserId, isCircle) { buildItems(state.messages, state.myUserId, isCircle) }

    LaunchedEffect(listState, state.hasMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisible ->
                if (state.hasMore && lastVisible >= items.size - 5) onAction(ConversationAction.OnLoadOlder)
            }
    }

    LaunchedEffect(state.messages.lastOrNull()?.clientId, state.messages.size) {
        if (listState.firstVisibleItemIndex <= 2) listState.animateScrollToItem(0)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ConversationTopBar(state = state, onAction = onAction) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
        ) {
            ConversationBanner(type = state.type)

            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading && state.messages.isEmpty() -> AppListLoading(modifier = Modifier.fillMaxSize())

                    state.error != null && state.conversation == null -> AppErrorScreen(
                        errorDescription = state.error,
                        onRetry = { onAction(ConversationAction.OnReload) },
                    )

                    state.messages.isEmpty() -> Text(
                        text = stringResource(R.string.start_conversation_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                    )

                    else -> LazyColumn(
                        state = listState,
                        reverseLayout = true,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items, key = { it.key }) { item ->
                            when (item) {
                                is ChatListItem.DateItem -> DateSeparator(item.date)
                                is ChatListItem.MessageItem -> MessageRow(
                                    message = item.message,
                                    isMine = item.message.senderId == state.myUserId,
                                    showSender = item.showSender,
                                    isCircle = isCircle,
                                    onAction = onAction,
                                )
                            }
                        }
                        if (state.isLoadingOlder) {
                            item(key = "older") {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = state.showPiiWarning) { PiiWarning() }

            Composer(state = state, onAction = onAction)
        }
    }

    if (state.showShareSheet) {
        ShareResultSheet(
            results = state.results,
            onDismiss = { onAction(ConversationAction.OnShowShareSheet(false)) },
            onShare = { onAction(ConversationAction.OnShareResult(it)) },
        )
    }

    state.reportTarget?.let {
        AlertDialog(
            onDismissRequest = { onAction(ConversationAction.OnReportRequest(null)) },
            title = { Text(stringResource(R.string.report_title)) },
            text = { Text(stringResource(R.string.report_body)) },
            confirmButton = {
                TextButton(onClick = { onAction(ConversationAction.OnConfirmReport) }) {
                    Text(stringResource(R.string.report_message), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ConversationAction.OnReportRequest(null)) }) { Text(stringResource(R.string.Cancel)) }
            },
        )
    }

    state.blockTarget?.let { participant ->
        AlertDialog(
            onDismissRequest = { onAction(ConversationAction.OnBlockRequest(null)) },
            title = { Text(stringResource(R.string.block_title, participant.name)) },
            text = { Text(stringResource(R.string.block_body)) },
            confirmButton = {
                TextButton(onClick = { onAction(ConversationAction.OnConfirmBlock) }) {
                    Text(stringResource(R.string.block), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ConversationAction.OnBlockRequest(null)) }) { Text(stringResource(R.string.Cancel)) }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationTopBar(state: ConversationState, onAction: (ConversationAction) -> Unit) {
    val conversation = state.conversation
    val isCircle = state.type == ConversationType.CIRCLE
    var menuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        title = {
            if (conversation != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(
                        name = conversation.title,
                        colors = conversationColors(conversation.type),
                        size = 40.dp,
                        shape = if (isCircle) MaterialTheme.shapes.small else CircleShape,
                    )
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = conversation.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false),
                            )
                            if (conversation.verified) {
                                Spacer(Modifier.width(4.dp))
                                VerifiedBadge(size = 15.dp)
                            }
                        }
                        val subtitle = when {
                            isCircle -> pluralStringResource(R.plurals.members_count, conversation.members, conversation.members)
                            state.otherParticipant?.role == ChatRole.PEER -> stringResource(R.string.trained_peer)
                            else -> conversation.subtitle
                        }
                        if (subtitle.isNotBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (conversation.type == ConversationType.DOCTOR) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        },
        actions = {
            if (conversation != null) {
                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options))
                    }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(if (conversation.muted) R.string.unmute else R.string.mute)) },
                            onClick = {
                                menuOpen = false
                                onAction(ConversationAction.OnToggleMute)
                            },
                        )
                        if (isCircle) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.leave_circle), color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    menuOpen = false
                                    onAction(ConversationAction.OnLeaveCircle)
                                },
                            )
                        } else {
                            state.otherParticipant?.let { participant ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.block), color = MaterialTheme.colorScheme.error) },
                                    onClick = {
                                        menuOpen = false
                                        onAction(ConversationAction.OnBlockRequest(participant))
                                    },
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun ConversationBanner(type: String) {
    when (type) {
        ConversationType.DOCTOR -> InfoBanner(
            text = stringResource(R.string.doctor_banner),
            icon = Icons.Outlined.Lock,
            container = MaterialTheme.colorScheme.tertiaryContainer,
            content = MaterialTheme.colorScheme.onTertiaryContainer,
        )

        ConversationType.PEER -> InfoBanner(
            text = stringResource(R.string.peer_banner),
            icon = Icons.Outlined.Shield,
            container = MaterialTheme.extendedColors.coralContainer,
            content = MaterialTheme.extendedColors.onCoralContainer,
        )

        ConversationType.CIRCLE -> InfoBanner(
            text = stringResource(R.string.circle_banner),
            icon = Icons.Outlined.Groups,
            container = MaterialTheme.extendedColors.coralContainer,
            content = MaterialTheme.extendedColors.onCoralContainer,
        )
    }
}

@Composable
private fun DateSeparator(date: LocalDate) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = dayLabel(date),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 5.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MessageRow(
    message: ChatMessage,
    isMine: Boolean,
    showSender: Boolean,
    isCircle: Boolean,
    onAction: (ConversationAction) -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    val bubbleShape = if (isMine) {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 6.dp)
    } else {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 6.dp, bottomEnd = 18.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
    ) {
        if (showSender) {
            Text(
                text = message.senderName,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.extendedColors.coral,
                modifier = Modifier.padding(start = 6.dp, bottom = 2.dp),
            )
        }
        Box {
            val baseModifier = Modifier
                .widthIn(max = 290.dp)
                .clip(bubbleShape)
                .combinedClickable(
                    onClick = { if (message.failed) onAction(ConversationAction.OnRetry(message)) },
                    onLongClick = { if (!isMine && message.id > 0) menuOpen = true },
                )

            if (message.kind == MessageKind.RESULT) {
                ResultBubble(result = message.result, isMine = isMine, modifier = baseModifier)
            } else {
                Column(
                    modifier = baseModifier
                        .background(if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .then(if (isMine) Modifier else Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, bubbleShape))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    )
                    MessageMeta(message = message, isMine = isMine, modifier = Modifier.align(Alignment.End))
                }
            }

            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.report_message)) },
                    onClick = {
                        menuOpen = false
                        onAction(ConversationAction.OnReportRequest(message))
                    },
                )
                if (isCircle && message.senderRole != ChatRole.DOCTOR) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.message_privately)) },
                        onClick = {
                            menuOpen = false
                            onAction(ConversationAction.OnMessagePrivately(message.senderId))
                        },
                    )
                }
                if (isCircle) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.block_sender, message.senderName.ifBlank { stringResource(R.string.this_person) }), color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            menuOpen = false
                            onAction(
                                ConversationAction.OnBlockRequest(
                                    ChatParticipant(userId = message.senderId, name = message.senderName, role = message.senderRole)
                                )
                            )
                        },
                    )
                }
            }
        }
        if (message.failed) {
            Text(
                text = stringResource(R.string.not_sent_retry),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 2.dp, end = 4.dp)
                    .clickable(role = Role.Button) { onAction(ConversationAction.OnRetry(message)) },
            )
        }
    }
}

@Composable
private fun MessageMeta(message: ChatMessage, isMine: Boolean, modifier: Modifier = Modifier) {
    val color = if (isMine) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f) else MaterialTheme.colorScheme.onSurfaceVariant
    Row(modifier = modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        if (message.pending) {
            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = color, modifier = Modifier.size(11.dp))
            Spacer(Modifier.width(3.dp))
        }
        Text(text = clockTime(message.createdAt), style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
private fun ResultBubble(result: SharedResult?, isMine: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(260.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = stringResource(R.string.shared_result).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = result?.let { resultTitle(it.results) } ?: stringResource(R.string.result_pending),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                val details = listOfNotNull(result?.date?.takeIf { it.isNotBlank() }, if (isMine) stringResource(R.string.share_result_note) else null)
                if (details.isNotEmpty()) {
                    Text(
                        text = details.joinToString(" · "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun resultTitle(results: String): String =
    results.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() } ?: stringResource(R.string.result_pending)

@Composable
private fun PiiWarning() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.extendedColors.amberContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = null,
            tint = MaterialTheme.extendedColors.onAmberContainer,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(R.string.pii_warning),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.extendedColors.onAmberContainer,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun Composer(state: ConversationState, onAction: (ConversationAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.canShareResults) {
            IconButton(
                onClick = { onAction(ConversationAction.OnShowShareSheet(true)) },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Icon(Icons.Outlined.AttachFile, contentDescription = stringResource(R.string.attach_result))
            }
        }
        OutlinedTextField(
            value = state.draft,
            onValueChange = { onAction(ConversationAction.OnDraftChange(it)) },
            placeholder = { Text(stringResource(R.string.message_hint)) },
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp),
            shape = RoundedCornerShape(24.dp),
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            ),
        )
        FilledIconButton(
            onClick = { onAction(ConversationAction.OnSend) },
            enabled = state.draft.isNotBlank() && state.conversation != null,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.send))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShareResultSheet(
    results: List<TestResult>,
    onDismiss: () -> Unit,
    onShare: (TestResult) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.share_result_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            )
            Text(
                text = stringResource(R.string.share_result_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (results.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_results_to_share),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
            results.take(10).forEach { result ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
                        .clickable(role = Role.Button) { onShare(result) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = resultTitle(result.results),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        )
                        Text(
                            text = result.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.attach_result), tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
