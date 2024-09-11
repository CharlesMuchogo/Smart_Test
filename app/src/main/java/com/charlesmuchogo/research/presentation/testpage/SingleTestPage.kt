package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.presentation.common.CenteredColumn

class SingleTestPage: Screen {
    @Composable
    override fun Content() {
        CenteredColumn {
            Text(text = "Single Test Page")
        }
    }
}

@Composable
fun SingleTestScreen(modifier: Modifier = Modifier) {
    CenteredColumn {
        Text(text = "Single Test Page")
    }
}