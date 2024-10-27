package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    modifier: Modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Button(
        shape = RoundedCornerShape(12),
        onClick = onClick,
        modifier = modifier.height(54.dp),
        enabled = enabled
    ) {
        content()
    }
}
