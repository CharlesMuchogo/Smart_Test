package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.ExoPlayerView
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

