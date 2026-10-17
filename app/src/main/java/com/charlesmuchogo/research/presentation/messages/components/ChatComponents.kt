package com.charlesmuchogo.research.presentation.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.ui.theme.extendedColors
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AvatarColors(val container: Color, val content: Color)

@Composable
fun conversationColors(type: String): AvatarColors = when (type) {
    ConversationType.DOCTOR -> AvatarColors(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
    else -> AvatarColors(MaterialTheme.extendedColors.coralContainer, MaterialTheme.extendedColors.onCoralContainer)
}

fun initialsOf(name: String): String {
    val parts = name.replace("Dr.", "").trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 && parts[0].length >= 2 -> {
            val word = parts[0]
            val second = word.drop(1).firstOrNull { it.isUpperCase() } ?: word[1]
            "${word.first()}$second".uppercase()
        }
        parts.size == 1 -> parts[0].take(1).uppercase()
        else -> "?"
    }
}

@Composable
fun Avatar(
    name: String,
    colors: AvatarColors,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = CircleShape,
    online: Boolean = false,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(colors.container),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initialsOf(name),
                color = colors.content,
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.32f).sp,
            )
        }
        if (online) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.28f)
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColors.success)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
            )
        }
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier, size: Dp = 16.dp) {
    Icon(
        imageVector = Icons.Filled.Verified,
        contentDescription = stringResource(R.string.verified_doctor),
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = modifier.size(size),
    )
}

@Composable
fun Pill(text: String, container: Color, content: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = content,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(container)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}

@Composable
fun InfoBanner(
    text: String,
    icon: ImageVector,
    container: Color,
    content: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(container)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        Text(
            text = text,
            color = content,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

fun parseInstant(value: String?): Instant? = value?.let { runCatching { Instant.parse(it) }.getOrNull() }

fun clockTime(value: String?): String {
    val instant = parseInstant(value) ?: return ""
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "%02d:%02d".format(local.hour, local.minute)
}

fun localDate(value: String?): LocalDate? =
    parseInstant(value)?.toLocalDateTime(TimeZone.currentSystemDefault())?.date

fun today(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

@Composable
fun dayLabel(date: LocalDate): String {
    val now = today()
    return when (date) {
        now -> stringResource(R.string.today)
        LocalDate.fromEpochDays(now.toEpochDays() - 1) -> stringResource(R.string.yesterday)
        else -> "${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)} ${date.year}"
    }
}

@Composable
fun inboxTime(value: String?): String {
    val date = localDate(value) ?: return ""
    return if (date == today()) clockTime(value) else dayLabel(date)
}

@Composable
fun SegmentedTabs(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(if (selected) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .selectable(selected = selected, role = Role.Tab, onClick = { onSelect(index) }),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                    ),
                    color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun FilterChipRow(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .heightIn(min = 36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface)
                    .border(
                        width = 1.dp,
                        color = if (selected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(18.dp),
                    )
                    .selectable(selected = selected, role = Role.Tab, onClick = { onSelect(index) })
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    ),
                    color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier),
    ) {
        content()
    }
}
