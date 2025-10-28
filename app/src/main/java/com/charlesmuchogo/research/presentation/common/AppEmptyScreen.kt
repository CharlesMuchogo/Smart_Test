package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
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
fun AppEmptyScreen(
    modifier: Modifier = Modifier,
    image: Int = R.drawable.alone,
    pageDescription: String = "Empty. Come back later",
) {
    CenteredColumn(modifier = modifier) {
        Image(
            painter = painterResource(image),
            contentDescription = pageDescription,
            modifier = Modifier.fillMaxHeight(0.4f).padding(horizontal = 12.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier.padding(12.dp),
            text = pageDescription,
            textAlign = TextAlign.Center,
            maxLines = 5,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}