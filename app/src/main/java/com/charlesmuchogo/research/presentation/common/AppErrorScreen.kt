package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R

@Composable
fun AppErrorScreen(
    modifier: Modifier = Modifier,
    image: Int = R.drawable.disconnect,
    errorDescription: String = "Something Went Wrong",
    onRetry: () -> Unit = {

    }
) {
    CenteredColumn(modifier = modifier) {
        Image(
            painter = painterResource(image),
            contentDescription = errorDescription,
            modifier = Modifier.fillMaxHeight(0.4f).padding(horizontal = 12.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier.padding(12.dp),
            text = errorDescription,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )

        Button(shape = MaterialTheme.shapes.extraLarge ,onClick = onRetry) {
            Text("Try again", modifier = Modifier)
        }
    }
}