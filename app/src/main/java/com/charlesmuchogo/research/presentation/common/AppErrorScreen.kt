package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R

private val offlineHints = listOf(
    "internet",
    "connect",
    "unable to resolve host",
    "timeout",
    "timed out",
    "network",
)

private val technicalHints = listOf(
    "exception",
    "http",
    "null",
    "{",
    "java.",
    "kotlin.",
    "serializ",
    "unexpected",
)

fun isOfflineError(message: String?): Boolean {
    val text = message?.lowercase() ?: return false
    return offlineHints.any { text.contains(it) }
}

fun isReadableError(message: String?): Boolean {
    val text = message?.trim()?.lowercase() ?: return false
    if (text.isBlank() || text.length > 160) return false
    return technicalHints.none { text.contains(it) }
}

@Composable
fun AppErrorScreen(
    modifier: Modifier = Modifier,
    errorDescription: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    val offline = isOfflineError(errorDescription)
    val body = when {
        offline -> stringResource(R.string.error_offline_body)
        isReadableError(errorDescription) -> errorDescription.orEmpty().trim()
        else -> stringResource(R.string.error_generic_body)
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        if (offline) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.errorContainer
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (offline) Icons.Outlined.CloudOff else Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = if (offline) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(34.dp),
                )
            }
            Text(
                text = stringResource(if (offline) R.string.error_offline_title else R.string.error_generic_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (onRetry != null) {
                Button(
                    onClick = onRetry,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(
                        text = stringResource(R.string.retry),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}
