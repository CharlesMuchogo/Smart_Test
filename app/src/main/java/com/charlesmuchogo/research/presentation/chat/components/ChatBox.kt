package com.charlesmuchogo.research.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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


    Row(
        modifier = modifier
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
            onClick = {
                onAction(ChatAction.OnSubmitMessage)
            },
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
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