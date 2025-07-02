package com.charlesmuchogo.research.presentation.chat.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charlesmuchogo.research.domain.models.Message
import com.charlesmuchogo.research.presentation.chat.MessageSender
import com.charlesmuchogo.research.presentation.utils.convertTimestampToTime
import kotlinx.datetime.Clock


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    message: Message,
    selected: Boolean = false,
) {


    val annotatedText = remember(message.message) {
        buildAnnotatedStringWithLinks(message.message)
    }

    val isUserMessage = message.sender == MessageSender.ME.name


    Column(
        modifier = modifier
            .fillMaxWidth().background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .padding(vertical = 1.dp)
    ) {
        Box(
            modifier =
                modifier
                    .align(if (isUserMessage) Alignment.End else Alignment.Start)
                    .padding(
                        start = if (isUserMessage) 40.dp else 0.dp,
                        end = if (isUserMessage) 0.dp else 40.dp,
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 30f,
                            topEnd = 30f,
                            bottomStart = if (isUserMessage) 30f else 0f,
                            bottomEnd = if (isUserMessage) 0f else 30f,
                        ),
                    )
                    .background(
                        if (isUserMessage) MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.6f
                        ) else MaterialTheme.colorScheme.surfaceVariant,
                    )
                    .padding(8.dp)
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.End,
            ) {

                ClickableText(
                    modifier = modifier
                        .padding(
                            bottom = 4.dp,
                            end = if (isUserMessage) 28.dp else 20.dp,
                        ),
                    text = annotatedText,
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    onClick = { offset ->

                    }
                )

                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = convertTimestampToTime(message.timestamp),
                        modifier = Modifier.alpha(0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
fun buildAnnotatedStringWithLinks(
    text: String,
    onClickLinkTag: String = "LINK",
    onClickEmailTag: String = "EMAIL"
): AnnotatedString {
    val urlRegex = Regex("""(https?://\S+)""")
    val emailRegex = Regex("""[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+""")
    val doubleBoldRegex = Regex("""\*\*(.+?)\*\*""")
    val singleBoldRegex = Regex("""(?<!\*)\*(?!\*)(.+?)(?<!\*)\*(?!\*)""")
    val codeRegex = Regex("""```(.*?)```""")

    val builder = AnnotatedString.Builder()
    var currentIndex = 0

    val matches = (
            urlRegex.findAll(text) +
                    emailRegex.findAll(text) +
                    codeRegex.findAll(text) +
                    doubleBoldRegex.findAll(text) +
                    singleBoldRegex.findAll(text)
            ).sortedBy { it.range.first }

    for (match in matches) {
        val start = match.range.first
        val end = match.range.last + 1

        // Append normal text before match
        if (currentIndex < start) {
            builder.append(text.substring(currentIndex, start))
        }

        val matchText = match.value

        when {
            codeRegex.matches(matchText) -> {
                val code = codeRegex.find(matchText)?.groups?.get(1)?.value ?: matchText
                builder.withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0xFFE0E0E0),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                ) {
                    builder.append(code)
                }
            }

            doubleBoldRegex.matches(matchText) -> {
                val bold = doubleBoldRegex.find(matchText)?.groups?.get(1)?.value ?: matchText
                builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    builder.append(bold)
                }
            }

            singleBoldRegex.matches(matchText) -> {
                val bold = singleBoldRegex.find(matchText)?.groups?.get(1)?.value ?: matchText
                builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    builder.append(bold)
                }
            }

            urlRegex.matches(matchText) -> {
                builder.pushStringAnnotation(tag = onClickLinkTag, annotation = matchText)
                builder.withStyle(
                    SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    builder.append(matchText)
                }
                builder.pop()
            }

            emailRegex.matches(matchText) -> {
                builder.pushStringAnnotation(tag = onClickEmailTag, annotation = matchText)
                builder.withStyle(
                    SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    builder.append(matchText)
                }
                builder.pop()
            }
        }

        currentIndex = end
    }

    // Append remaining text
    if (currentIndex < text.length) {
        builder.append(text.substring(currentIndex))
    }

    return builder.toAnnotatedString()
}

@Composable
@Preview()
fun ChatItemPreview() {
    val testText = """
Visit **Main points** or *just bold*. 
See https://example.com or email test@example.com. 
Here's code: ```println("Hello")```
""".trimIndent()
    val message = Message(
        id = 1,
        message = testText,
        sender = MessageSender.AI.name,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )


    Surface(
        color = MaterialTheme.colorScheme.background
    ){
        ChatItem(
            selected = true,
            message = message,

        )
    }
}

