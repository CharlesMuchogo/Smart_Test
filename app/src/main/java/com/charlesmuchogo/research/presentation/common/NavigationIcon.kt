package com.charlesmuchogo.research.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.charlesmuchogo.research.navController

@Composable
fun NavigationIcon(modifier: Modifier = Modifier, imageVector: ImageVector = Icons.Default.ArrowBackIosNew) {
    IconButton(
        modifier = modifier,

        onClick = { navController.popBackStack() }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Back"
        )
    }
}

@Composable
fun TopBarTitle(
    title: String,
    style: TextStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
) {
    Text(title, style = style )
}
