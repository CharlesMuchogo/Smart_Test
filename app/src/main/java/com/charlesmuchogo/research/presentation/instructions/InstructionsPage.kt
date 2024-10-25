package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.ExoPlayerView

class InstructionsPage : Screen {
    @Composable
    override fun Content() {
        CenteredColumn(modifier = Modifier) {
            Text(text = "Instructions Page")
        }
    }
}

@Composable
fun InstructionsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ExoPlayerView()
            }

            item {
                Text(
                    text = FIRST_HEADING, style = MaterialTheme.typography.bodyLarge
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
                Image(
                    painter = painterResource(R.drawable.two),
                    contentDescription = "Image"
                )
            }

            item {
                Image(
                    painter = painterResource(R.drawable.three),
                    contentDescription = "Image"
                )
            }

                item {
                    RegularBodyText(text = sentence21)
                }


            item {
                Image(
                    painter = painterResource(R.drawable.four),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = sentence22)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.five),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = sentence22)
            }


            item {
                Image(
                    painter = painterResource(R.drawable.six),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.seven),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.eight),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.nine),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.ten),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.eleven),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.twelve),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.thirteen),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence22)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.fourteen),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = sentence22)
            }


        }
    }
}

@Composable
fun RegularBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = Modifier,
        text = text, style = MaterialTheme.typography.bodyMedium
            .copy(fontWeight = FontWeight.Normal)
    )
}

@Preview
@Composable
fun InstructionsScreenPreview() {
    InstructionsScreen()
}
