package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun AppListLoading(modifier: Modifier = Modifier.fillMaxSize()) {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (i in 1..32) {
                ShimmeringPlaceholder()
            }
        }
    }
}

@Composable
fun ShimmeringPlaceholder() {
    Row(
        modifier =
            Modifier
                .shimmer()
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(80.dp, 80.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.LightGray),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.LightGray),
            )
            Box(
                modifier =
                    Modifier
                        .size(120.dp, 20.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.LightGray),
            )
        }
    }
}
