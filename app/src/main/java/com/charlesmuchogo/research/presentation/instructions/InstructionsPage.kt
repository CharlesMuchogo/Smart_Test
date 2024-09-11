package com.charlesmuchogo.research.presentation.instructions

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.presentation.common.CenteredColumn

class InstructionsPage:Screen {
    @Composable
    override fun Content() {
        CenteredColumn(modifier = Modifier) {
            Text(text = "Instructions Page")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InstructionsScreen(modifier: Modifier = Modifier) {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {},
            icon = { Icon(imageVector = Icons.Filled.Add, "Extended floating action button.") },
            text = { Text(text = "Take a test") },
        )
    }) {
        CenteredColumn(modifier = Modifier) {
            Text(text = "Instructions Page")
        }
    }
}