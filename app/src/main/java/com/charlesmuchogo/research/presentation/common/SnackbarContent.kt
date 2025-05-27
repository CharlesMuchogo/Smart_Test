
package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.domain.models.SnackBarItem
import ui.theme.greenLight

@Composable
fun SnackBarContent(
    modifier: Modifier = Modifier,
    description: String = "",
    snackBarHostState: SnackbarHostState,
    alignBottom: Boolean = false,
) {
    Box(
        modifier = modifier.fillMaxSize().testTag(description),
        contentAlignment = if (alignBottom) Alignment.BottomCenter else Alignment.TopCenter,
    ) {
        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = {
                val message = it.visuals.message
                val isError = message.lowercase().contains("error") || message.lowercase().contains("something")
                Card(
                    modifier =
                        Modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                snackBarHostState.currentSnackbarData?.dismiss()
                            },
                    border =
                        BorderStroke(
                            1.dp,
                            if (isError) MaterialTheme.colorScheme.error else greenLight,
                        ),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(onClick = {
                            snackBarHostState.currentSnackbarData?.dismiss()
                        }) {
                            Icon(
                                imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                                tint = if (isError) MaterialTheme.colorScheme.error else greenLight,
                                contentDescription = null,
                            )
                        }
                        Text(
                            modifier =
                                Modifier
                                    .weight(1f),
                            text = it.visuals.message,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onBackground,
                                ),
                        )


                    }
                }
            },
        )
    }
}
