package com.charlesmuchogo.research.presentation.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.presentation.common.CenteredColumn

class ProfilePage:Screen {
    @Composable
    override fun Content() {
        CenteredColumn(modifier = Modifier) {
            Text(text = "Profile Page")
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    CenteredColumn(modifier = Modifier) {
        Text(text = "Profile Page")
    }
}