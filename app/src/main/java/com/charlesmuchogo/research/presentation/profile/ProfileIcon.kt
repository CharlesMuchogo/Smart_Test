package com.charlesmuchogo.research.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ProfileIcon(
    image: String?,
    modifier: Modifier = Modifier,
    photoSize: Dp = 50.dp,
    iconSize: Dp = 30.dp,
    icon: ImageVector = Icons.Default.Person,
    onclick: () -> Unit,
) {
    val hasImage = !image.isNullOrBlank()
    val photoModifier = modifier.size(photoSize).clip(CircleShape)
    Box(
        modifier =
            photoModifier
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onclick.invoke() },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (hasImage) {
            SubcomposeAsyncImage(
                modifier =
                    Modifier.fillMaxSize().clip(
                        RoundedCornerShape(4),
                    ),
                model = image,
                contentScale = ContentScale.Crop,
                contentDescription = image,
                loading = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier =
                                Modifier
                                    .size(30.dp),
                        )
                    }
                },
                error = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimary,
                            imageVector = icon,
                            contentDescription = image,
                            modifier = Modifier.size(iconSize),
                        )
                    }
                },
            )
        } else {
            Icon(
                tint = MaterialTheme.colorScheme.onPrimary,
                imageVector = icon,
                contentDescription = image,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
