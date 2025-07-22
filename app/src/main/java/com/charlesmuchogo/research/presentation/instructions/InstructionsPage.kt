package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.showInterstitialAd
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.ExoPlayerView
import kotlinx.coroutines.delay

@Composable
fun InstructionsRoot() {
    val viewModel = hiltViewModel<InstructionsScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    InstructionsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    state: InstructionsState,
    onAction: (InstructionsAction) -> Unit
) {


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.showAd) {
            delay(3_000L)
            showInterstitialAd(context, onShowAd = {
                onAction(InstructionsAction.OnHasShownAd)
            })
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                ExoPlayerView()
            }

            item {
                Text(
                    text = stringResource(R.string.first_heading),
                    style = MaterialTheme.typography.bodyLarge
                        .copy(fontWeight = FontWeight.Medium)
                )
            }

            item {
                Image(
                    painter = painterResource(R.drawable.one),
                    contentDescription = "Image"
                )
            }


            item {
                HeaderText(
                    text = stringResource(R.string.sentence_1),
                    style = MaterialTheme.typography.bodyLarge
                        .copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error
                        )
                )
            }

            item {
                Image(
                    painter = painterResource(R.drawable.two),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = stringResource(R.string.sentence_2))
            }

            item {
                Image(
                    painter = painterResource(R.drawable.three),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = stringResource(R.string.sentence_3))
            }


            item {
                Image(
                    painter = painterResource(R.drawable.four),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = stringResource(R.string.sentence_22))
            }

            item {
                Image(
                    painter = painterResource(R.drawable.five),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = stringResource(R.string.sentence_23))
            }


            item {
                Image(
                    painter = painterResource(R.drawable.six),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_27))
            }



            item {
                Image(
                    painter = painterResource(R.drawable.seven),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_28))
            }



            item {
                Image(
                    painter = painterResource(R.drawable.eight),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_26))
            }



            item {
                Image(
                    painter = painterResource(R.drawable.nine),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_24))
            }



            item {
                Image(
                    painter = painterResource(R.drawable.ten),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_25))
            }




            item {
                Image(
                    painter = painterResource(R.drawable.eleven),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = stringResource(R.string.sentence_3))
            }



            item {
                HeaderText(text = stringResource(R.string.sentence_7))
            }

            item {
                Image(
                    painter = painterResource(R.drawable.twelve),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = stringResource(R.string.sentence_8))
            }

            item {
                RegularBodyText(text = stringResource(R.string.sentence_9))
            }

            item {
                HeaderText(text = stringResource(R.string.sentence_15))
            }

            item {
                Image(
                    painter = painterResource(R.drawable.thirteen),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = stringResource(R.string.sentence_16))
            }

            item {
                HeaderText(text = stringResource(R.string.sentence_19))
            }
            item {
                RegularBodyText(text = stringResource(R.string.sentence_18))
            }




            item {
                HeaderText(text = stringResource(R.string.sentence_12))
            }

            item {
                Image(
                    painter = painterResource(R.drawable.fourteen),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = stringResource(R.string.sentence_13))
            }


            item {
                HeaderText(text = stringResource(R.string.sentence_20))
                RegularBodyText(text = stringResource(R.string.sentence_6))
            }

            item {
                AppButton(onClick = {}) {
                    Text(text = stringResource(R.string.takeTest))
                }
            }
        }
    }
}

@Composable
fun RegularBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = text, style = MaterialTheme.typography.bodyMedium
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

