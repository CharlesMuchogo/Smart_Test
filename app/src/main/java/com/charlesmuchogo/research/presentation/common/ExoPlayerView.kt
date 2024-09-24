package com.charlesmuchogo.research.presentation.common

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.charlesmuchogo.research.R

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.instructions}")

    val mediaSource = remember(videoUri) {
        MediaItem.fromUri(videoUri)
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.setAudioAttributes(
            AudioAttributes.DEFAULT,
            true
        )
        exoPlayer.setHandleAudioBecomingNoisy(true)
        exoPlayer.setWakeMode(C.WAKE_MODE_LOCAL)
        exoPlayer.prepare()
    }

    val playerView = remember { PlayerView(context).apply {
        useController = true
        controllerHideOnTouch = true
        controllerShowTimeoutMs = 2000
        controllerAutoShow = false
    }}

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)){
        AndroidView(
            factory = { ctx ->
                playerView.apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
