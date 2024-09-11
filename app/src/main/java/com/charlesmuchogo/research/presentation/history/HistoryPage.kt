package com.charlesmuchogo.research.presentation.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.presentation.common.CenteredColumn

class HistoryPage:Screen {
    @Composable
    override fun Content() {
        CenteredColumn(modifier = Modifier) {
            Text(text = "History Page")
        }
    }
}

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    CenteredColumn(modifier = Modifier) {
        Text(text = "History Page")
    }
}