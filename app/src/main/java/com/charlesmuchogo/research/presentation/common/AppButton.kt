package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppButton(
    modifier: Modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(54.dp)
    ) {
        content()
    }
}