package com.charlesmuchogo.research.presentation.common

import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.charlesmuchogo.research.R

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var isControllerVisible by remember {  mutableStateOf(false)}

    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.instructions}")

    val mediaSource = remember(videoUri) {
        MediaItem.fromUri(videoUri)
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()

    }

    val playerView = remember { PlayerView(context).apply {
        useController = true
        controllerHideOnTouch = true
        controllerShowTimeoutMs = 0
    }}

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            playerView.apply {
                player = exoPlayer
                setOnClickListener {
                    isControllerVisible = !isControllerVisible
                    if (isControllerVisible) {
                        hideController()
                    } else {
                        showController()
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
