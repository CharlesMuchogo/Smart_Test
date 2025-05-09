package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredColumn(
    modifier: Modifier = Modifier,
    columnVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = columnVerticalArrangement,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}
