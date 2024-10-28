package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.charlesmuchogo.research.presentation.navigation.TestPage


@Composable
fun InstructionsScreen(modifier: Modifier = Modifier, navController: NavController, isKiswahiliLanguage: Boolean) {
    if (isKiswahiliLanguage) {
        KiswahiliInstructions(onClick = { navController.navigate(TestPage) })
    } else {
        EnglishInstructions(onClick = { navController.navigate(TestPage) })
    }

}

@Composable
fun RegularBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text, style = MaterialTheme.typography.bodyLarge
            .copy(fontWeight = FontWeight.Normal)
    )
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(fontWeight = FontWeight.SemiBold)
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text, style = style
    )
}

