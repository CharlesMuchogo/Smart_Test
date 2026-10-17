package com.charlesmuchogo.research.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.ads.rememberAdsManager
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.SubscribePage
import com.charlesmuchogo.research.presentation.chat.REWARDED_BONUS_MESSAGES
import com.charlesmuchogo.research.presentation.utils.findActivity
import com.charlesmuchogo.research.presentation.chat.ChatAction
import com.charlesmuchogo.research.presentation.chat.ChatState


@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    state: ChatState,
    onAction: (ChatAction) -> Unit,
) {

    /* val chatBoxPageState by viewModel.chatBoxPageState.collectAsStateWithLifecycle()

     val imagePicker = koinInject<ImagePicker>()

     imagePicker.RegisterPicker { img, fileName ->
         viewModel.onAction(MessageAction.OnFileChange(file = img, fileName = fileName))
     }

     AnimatedVisibility(chatBoxPageState.showImagePicker) {
         AppImagePickerDialog(
             onDismiss = { viewModel.onAction(MessageAction.OnUpdateShowImage(false)) },
             onPickImage = {
                 viewModel.onAction(MessageAction.OnUpdateShowImage(false))
                 imagePicker.pickImage()
             },
             onCaptureImage = {
                 viewModel.onAction(MessageAction.OnUpdateShowImage(false))
                 imagePicker.captureImage()
             },
         )
     }*/


    val limitReached = !state.isSubscribed && state.dailyMessageCount >= state.dailyMessageLimit

    Column(modifier = modifier) {
        if (!state.isSubscribed) {
            Text(
                text = if (limitReached) {
                    stringResource(R.string.chat_daily_limit_reached)
                } else {
                    stringResource(
                        R.string.chat_messages_remaining_today,
                        state.dailyMessageLimit - state.dailyMessageCount
                    )
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (limitReached) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            if (limitReached) {
                ChatLimitActions(onRewardEarned = { onAction(ChatAction.OnRewardEarned) })
            }
        }

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextField(
            value = state.message,
            onValueChange = { newText ->
                onAction(ChatAction.OnMessageChange(newText))
            },
            maxLines = 6,
            trailingIcon = {
                /* if (chatBoxPageState.fileName == null) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         IconButton(onClick = {
                             viewModel.onAction(
                                 action = MessageAction.OnMessageTypeChange(
                                     MessageType.Document.name
                                 )
                             )
                             imagePicker.pickFile()

                         }) {
                             Icon(
                                 imageVector = Icons.Default.AttachFile,
                                 contentDescription = "Attach",
                             )
                         }

                         IconButton(onClick = {
                             viewModel.onAction(
                                 action = MessageAction.OnMessageTypeChange(
                                     MessageType.Image.name
                                 )
                             )
                             viewModel.onAction(MessageAction.OnUpdateShowImage(show = true))
                         }) {
                             Icon(
                                 imageVector = Icons.Outlined.PhotoCamera,
                                 contentDescription = "Attach",
                             )
                         }
                     }
                 } */
            },
            modifier =
                Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                        },
                    ),
            shape = MaterialTheme.shapes.extraLarge,
            colors =
                TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            placeholder = {
                Text(text = "Type something...")
            },
        )

        IconButton(
            enabled = !limitReached,
            onClick = {
                onAction(ChatAction.OnSubmitMessage)
            },
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(
                        color = if (limitReached) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                    .align(Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier,
            )
        }
    }
    }
}


@Composable
@Preview()
fun ChatBoxPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ){
        ChatBox(
            state = ChatState(),
            onAction = {},
        )
    }
}

@Composable
private fun ChatLimitActions(onRewardEarned: () -> Unit) {
    val adsManager = rememberAdsManager()
    val context = LocalContext.current
    val rewardedReady by adsManager.rewardedReady.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (adsManager.rewardedSupported && rewardedReady) {
            OutlinedButton(
                onClick = {
                    context.findActivity()?.let { activity ->
                        adsManager.showRewarded(activity, onRewarded = onRewardEarned)
                    }
                },
                modifier = Modifier.heightIn(min = 44.dp),
            ) {
                Icon(Icons.Outlined.OndemandVideo, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    text = stringResource(R.string.watch_ad_for_messages, REWARDED_BONUS_MESSAGES),
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
        TextButton(
            onClick = { navController.navigate(SubscribePage) },
            modifier = Modifier.heightIn(min = 44.dp),
        ) {
            Text(stringResource(R.string.go_unlimited), fontWeight = FontWeight.Bold)
        }
    }
}
