package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.showInterstitialAd
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager.Companion.INSTRUCTIONS_AD_UNIT_ID
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.PendingTestPage
import com.charlesmuchogo.research.navigation.TestPage
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.ExoPlayerView
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import androidx.compose.runtime.derivedStateOf

@Composable
fun InstructionsRoot() {
    val viewModel = hiltViewModel<InstructionsScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    InstructionsScreen(state = state, onAction = viewModel::onAction)
}

@Composable
private fun AnimatedListItem(
    index: Int,
    listState: androidx.compose.foundation.lazy.LazyListState,
    content: @Composable () -> Unit
) {
    val isVisible = remember(listState) {
        derivedStateOf {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            visibleItems.any { it.index == index }
        }
    }

    var hasBeenVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible.value) {
        if (isVisible.value) hasBeenVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (hasBeenVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 420,
            delayMillis = (index % 3) * 60, // slight stagger per group of 3
            easing = EaseOutCubic
        ),
        label = "itemAlpha_$index"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (hasBeenVisible) 0f else 28f,
        animationSpec = tween(
            durationMillis = 420,
            delayMillis = (index % 3) * 60,
            easing = EaseOutCubic
        ),
        label = "itemOffsetY_$index"
    )

    Box(
        modifier = Modifier
            .alpha(alpha)
            .offset { IntOffset(x = 0, y = offsetY.roundToInt()) }
    ) {
        content()
    }
}

// Shorthand so every LazyColumn item() block stays a one-liner.
@Composable
private fun LazyListScope_AnimatedItem(
    index: Int,
    listState: androidx.compose.foundation.lazy.LazyListState,
    content: @Composable () -> Unit
) = AnimatedListItem(index = index, listState = listState, content = content)



@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    state: InstructionsState,
    onAction: (InstructionsAction) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (state.showAd) {
            delay(3_000L)
            showInterstitialAd(context, onShowAd = {
                onAction(InstructionsAction.OnHasShownAd)
            }, INSTRUCTIONS_AD_UNIT_ID)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // index 0 — video player (no animation; plays immediately)
            item { ExoPlayerView() }

            item {
                AnimatedListItem(index = 1, listState = listState) {
                    Text(
                        text = stringResource(R.string.first_heading),
                        style = MaterialTheme.typography.bodyLarge
                            .copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            item {
                AnimatedListItem(index = 2, listState = listState) {
                    Image(painter = painterResource(R.drawable.one), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 3, listState = listState) {
                    HeaderText(
                        text = stringResource(R.string.sentence_1),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }

            item {
                AnimatedListItem(index = 4, listState = listState) {
                    Image(painter = painterResource(R.drawable.two), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 5, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_2))
                }
            }

            item {
                AnimatedListItem(index = 6, listState = listState) {
                    Image(painter = painterResource(R.drawable.three), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 7, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_3))
                }
            }

            item {
                AnimatedListItem(index = 8, listState = listState) {
                    Image(painter = painterResource(R.drawable.four), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 9, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_22))
                }
            }

            item {
                AnimatedListItem(index = 10, listState = listState) {
                    Image(painter = painterResource(R.drawable.five), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 11, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_23))
                }
            }

            item {
                AnimatedListItem(index = 12, listState = listState) {
                    Image(painter = painterResource(R.drawable.six), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 13, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_27))
                }
            }

            item {
                AnimatedListItem(index = 14, listState = listState) {
                    Image(painter = painterResource(R.drawable.seven), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 15, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_28))
                }
            }

            item {
                AnimatedListItem(index = 16, listState = listState) {
                    Image(painter = painterResource(R.drawable.eight), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 17, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_26))
                }
            }

            item {
                AnimatedListItem(index = 18, listState = listState) {
                    Image(painter = painterResource(R.drawable.nine), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 19, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_24))
                }
            }

            item {
                AnimatedListItem(index = 20, listState = listState) {
                    Image(painter = painterResource(R.drawable.ten), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 21, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_25))
                }
            }

            item {
                AnimatedListItem(index = 22, listState = listState) {
                    Image(painter = painterResource(R.drawable.eleven), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 23, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_3))
                }
            }

            item {
                AnimatedListItem(index = 24, listState = listState) {
                    HeaderText(text = stringResource(R.string.sentence_7))
                }
            }

            item {
                AnimatedListItem(index = 25, listState = listState) {
                    Image(painter = painterResource(R.drawable.twelve), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 26, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_8))
                }
            }

            item {
                AnimatedListItem(index = 27, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_9))
                }
            }

            item {
                AnimatedListItem(index = 28, listState = listState) {
                    HeaderText(text = stringResource(R.string.sentence_15))
                }
            }

            item {
                AnimatedListItem(index = 29, listState = listState) {
                    Image(painter = painterResource(R.drawable.thirteen), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 30, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_16))
                }
            }

            item {
                AnimatedListItem(index = 31, listState = listState) {
                    HeaderText(text = stringResource(R.string.sentence_19))
                }
            }

            item {
                AnimatedListItem(index = 32, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_18))
                }
            }

            item {
                AnimatedListItem(index = 33, listState = listState) {
                    HeaderText(text = stringResource(R.string.sentence_12))
                }
            }

            item {
                AnimatedListItem(index = 34, listState = listState) {
                    Image(painter = painterResource(R.drawable.fourteen), contentDescription = null)
                }
            }

            item {
                AnimatedListItem(index = 35, listState = listState) {
                    RegularBodyText(text = stringResource(R.string.sentence_13))
                }
            }

            item {
                AnimatedListItem(index = 36, listState = listState) {
                    HeaderText(text = stringResource(R.string.sentence_20))
                }
            }

            item {
                AnimatedListItem(index = 37, listState = listState) {

                    RegularBodyText(text = stringResource(R.string.sentence_6))
                }
            }

            item {
                AnimatedListItem(index = 38, listState = listState) {
                    AppButton(onClick = {
                        when (state.hasPendingResults) {
                            true -> navController.navigate(PendingTestPage)
                            false -> navController.navigate(TestPage)
                        }
                    }) {
                        Text(text = stringResource(R.string.takeTest))
                    }
                }
            }
        }
    }
}

@Composable
fun RegularBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
    )
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text,
        style = style
    )
}