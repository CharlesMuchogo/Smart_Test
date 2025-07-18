package com.charlesmuchogo.research.presentation.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.showInterstitialAd
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.chat.components.ChatBox
import com.charlesmuchogo.research.presentation.chat.components.ChatItem
import com.charlesmuchogo.research.presentation.chat.components.TypingBubble
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import kotlinx.coroutines.delay

@Composable
fun ChatRoot() {

    val viewModel = hiltViewModel<ChatViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthControllerScreen(
        modifier = Modifier.statusBarsPadding().navigationBarsPadding(),
        screen = {
            ChatScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        authRequired = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    state: ChatState,
    onAction: (ChatAction) -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.showAd) {
            delay(3_000L)
            showInterstitialAd(context, onShowAd = {
                onAction(ChatAction.OnUpdateShowAd(false))
            })
        }
        onAction(ChatAction.FetchMessages)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Exit"
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(state.selectedMessages.isNotEmpty()) {
                        IconButton(onClick = {
                            onAction(ChatAction.OnReportMessages)
                        }) {
                            Icon(imageVector = Icons.Default.Report, contentDescription = "Report")
                        }
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.chat),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                    )
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {

            when (state.isLoading) {
                true -> {
                    AppListLoading(modifier = Modifier.weight(1f))
                }

                false -> {
                    when (state.messages.isEmpty()) {
                        true -> {
                            CenteredColumn(modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = painterResource(R.drawable.empty),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .padding(vertical = 16.dp),
                                    contentDescription = "Empty"
                                )
                                Text(
                                    "No messages yet. How are you doing?",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                                )
                            }
                        }

                        false -> {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                reverseLayout = true
                            ) {
                                item {
                                    AnimatedVisibility(visible = state.isGeneratingContent) {
                                        TypingBubble()
                                    }
                                }

                                state.messages.forEach { group ->
                                    items(group.value) {
                                        ChatItem(
                                            modifier = Modifier.combinedClickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                                onLongClick = {
                                                    onAction(ChatAction.OnSelectMessage(it))
                                                },
                                                onClick = {
                                                    if (state.selectedMessages.isNotEmpty()) {
                                                        onAction(ChatAction.OnSelectMessage(it))
                                                    }
                                                }
                                            ),
                                            selected = state.selectedMessages.contains(it),
                                            message = it,
                                        )
                                    }

                                    item {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier =
                                                    Modifier
                                                        .clip(
                                                            MaterialTheme.shapes.extraLarge,
                                                        )
                                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(
                                                        vertical = 8.dp,
                                                        horizontal = 16.dp
                                                    ),
                                                    text = group.key,
                                                    style = MaterialTheme.typography.titleSmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ChatBox(state = state, onAction = onAction)
                }
            }
        }
    }
}