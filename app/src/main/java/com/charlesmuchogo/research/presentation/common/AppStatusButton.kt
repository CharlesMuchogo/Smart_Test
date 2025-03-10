package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.theme.yellowLight

@Composable
fun AppStatusButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    containerColor: Color = yellowLight,
    contentColor: Color = MaterialTheme.colorScheme.background,
    height: Dp = 34.dp,
    width: Dp = 80.dp,
) {
    TextButton(
        shape = RoundedCornerShape(12),
        modifier = modifier.height(height).width(width),
        colors =
            ButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor,
            ),
        onClick = onClick,
    ) {
        Text(
            text = label.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            style =
                MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
        )
    }
}
