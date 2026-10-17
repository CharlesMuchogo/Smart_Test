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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.chat.Circle
import com.charlesmuchogo.research.domain.models.chat.MessagePrivacy
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ConversationPage
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.AvatarColors
import com.charlesmuchogo.research.presentation.messages.components.SegmentedTabs
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.ui.theme.extendedColors

@Composable
fun CommunityRoot() {
    val viewModel = hiltViewModel<CommunityViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.openConversation.collect { id -> navController.navigate(ConversationPage(id)) }
    }

    AuthControllerScreen(showTopBar = true) {
        CommunityScreen(state = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(state: CommunityState, onAction: (CommunityAction) -> Unit) {
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
                        text = stringResource(R.string.community),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    )
                },
                actions = {
                    state.profile?.alias?.takeIf { it.isNotBlank() }?.let { alias ->
                        Row(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(22.dp))
                                .clickable(role = Role.Button) { onAction(CommunityAction.OnShowIdentitySheet(true)) }
                                .padding(start = 6.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Avatar(
                                name = alias,
                                colors = AvatarColors(MaterialTheme.extendedColors.coralContainer, MaterialTheme.extendedColors.onCoralContainer),
                                size = 30.dp,
                            )
                            Text(
                                text = alias,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .widthIn(max = 120.dp),
                                maxLines = 1,
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { PeerBuddyCard(isMatching = state.isMatching, onClick = { onAction(CommunityAction.OnFindBuddy) }) }

            item {
                Text(
                    text = stringResource(R.string.support_circles),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                )
            }

            when {
                state.isLoading && state.circles.isEmpty() -> item { AppListLoading(modifier = Modifier.height(200.dp)) }

                state.error != null && state.circles.isEmpty() -> item {
                    AppErrorScreen(errorDescription = state.error, onRetry = { onAction(CommunityAction.Load) })
                }

                else -> items(state.circles, key = { it.id }) { circle ->
                    CircleRow(
                        circle = circle,
                        isJoining = state.joiningCircleId == circle.id,
                        onClick = { onAction(CommunityAction.OnCircleClick(circle)) },
                    )
                }
            }

            item {
                SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = stringResource(R.string.community_guidelines_note),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
            }
        }
    }

    if (state.showIdentitySheet) {
        IdentitySheet(state = state, onAction = onAction)
    }
}

@Composable
private fun PeerBuddyCard(isMatching: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.extendedColors.coral)
            .clickable(role = Role.Button, enabled = !isMatching, onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 30.dp, y = 40.dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.08f)),
        )
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val onCoral = MaterialTheme.extendedColors.coralContainer
            Text(
                text = stringResource(R.string.peer_buddy_label).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = onCoral,
            )
            Text(
                text = stringResource(R.string.peer_buddy_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.surface,
            )
            Text(
                text = stringResource(R.string.peer_buddy_body),
                style = MaterialTheme.typography.bodyMedium,
                color = onCoral,
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .heightIn(min = 40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (isMatching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.extendedColors.coral,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.find_buddy),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.extendedColors.onCoralContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun circleColors(index: Long): AvatarColors = when ((index % 4).toInt()) {
    0 -> AvatarColors(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
    1 -> AvatarColors(MaterialTheme.extendedColors.coralContainer, MaterialTheme.extendedColors.onCoralContainer)
    2 -> AvatarColors(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
    else -> AvatarColors(MaterialTheme.extendedColors.amberContainer, MaterialTheme.extendedColors.onAmberContainer)
}

@Composable
private fun CircleRow(circle: Circle, isJoining: Boolean, onClick: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(name = circle.title, colors = circleColors(circle.id), size = 46.dp, shape = MaterialTheme.shapes.medium)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(circle.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(
                    text = listOf(circle.description, pluralStringResource(R.plurals.members_count, circle.members, circle.members))
                        .filter { it.isNotBlank() }
                        .joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Button(
                onClick = onClick,
                enabled = !isJoining,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 14.dp),
                colors = if (circle.joined) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.surface,
                    )
                },
            ) {
                if (isJoining) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(if (circle.joined) R.string.open else R.string.join), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IdentitySheet(state: CommunityState, onAction: (CommunityAction) -> Unit) {
    val options = listOf(MessagePrivacy.DOCTORS_ONLY, MessagePrivacy.CIRCLES, MessagePrivacy.NONE)
    ModalBottomSheet(onDismissRequest = { onAction(CommunityAction.OnShowIdentitySheet(false)) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = stringResource(R.string.community_identity),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            )
            OutlinedTextField(
                value = state.aliasDraft,
                onValueChange = { onAction(CommunityAction.OnAliasChange(it)) },
                label = { Text(stringResource(R.string.community_name)) },
                supportingText = { Text(state.profileError ?: stringResource(R.string.community_name_hint)) },
                isError = state.profileError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(R.string.who_can_message),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
            SegmentedTabs(
                labels = listOf(
                    stringResource(R.string.privacy_doctors),
                    stringResource(R.string.privacy_circles),
                    stringResource(R.string.privacy_none),
                ),
                selectedIndex = options.indexOf(state.privacyDraft).coerceAtLeast(0),
                onSelect = { onAction(CommunityAction.OnPrivacyChange(options[it])) },
            )
            Button(
                onClick = { onAction(CommunityAction.OnSaveIdentity) },
                enabled = !state.isSavingProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                if (state.isSavingProfile) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.save), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
