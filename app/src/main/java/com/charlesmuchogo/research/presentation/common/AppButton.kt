package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
    onClick: () -> Unit,
    shape: Shape? = MaterialTheme.shapes.medium,
    height: Dp = 54.dp,
    content: @Composable () -> Unit,

    ) {
    Button(
        enabled = enabled,
        shape = shape ?: ButtonDefaults.shape,
        onClick = onClick,
        modifier = modifier.height(height),
    ) {
        content()
    }
}
