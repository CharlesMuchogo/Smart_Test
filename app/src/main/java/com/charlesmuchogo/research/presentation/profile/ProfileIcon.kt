package com.charlesmuchogo.research.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.charlesmuchogo.research.data.network.Http.Companion.httpUrlBuilder
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.navigation.PhotoPage

@Composable
fun ProfileIcon(
    image: String?,
    modifier: Modifier = Modifier,
    photoSize: Dp = 50.dp,
    iconSize: Dp = 30.dp,
    icon: ImageVector = Icons.Default.Person,
    onclick: () -> Unit = { navController.navigate(PhotoPage(image.toString()))},
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    modifier: Modifier = Modifier,
    url: String,
    title: String,
) {
    val imageUrl = if (url.first() == '/') httpUrlBuilder() + url else url
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier =
                    Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                    }
                },
                title = { Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)) },
            )
        },
    ) {
        Box(
            modifier =
                modifier.fillMaxSize().padding(it).pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offset =
                            Offset(
                                x = offset.x + pan.x,
                                y = offset.y + pan.y,
                            )
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                modifier =
                    Modifier.fillMaxHeight(0.4f).graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    ),
                model = imageUrl,
                contentScale = ContentScale.Fit,
                contentDescription = title,
                loading = {
                    CenteredColumn {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier =
                                Modifier
                                    .size(30.dp),
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            imageVector = Icons.Default.Image,
                            contentDescription = title,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                },
            )
        }
    }
}



