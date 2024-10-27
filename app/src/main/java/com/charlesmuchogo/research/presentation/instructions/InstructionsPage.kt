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
import androidx.compose.ui.text.TextStyle
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
            horizontalAlignment = Alignment.Start,
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
                HeaderText(
                    text = sentence1,
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
                RegularBodyText(text = sentence2)
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
                RegularBodyText(text = sentence23)
            }


            item {
                Image(
                    painter = painterResource(R.drawable.six),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence27)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.seven),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence28)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.eight),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence26)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.nine),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence24)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.ten),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence25)
            }




            item {
                Image(
                    painter = painterResource(R.drawable.eleven),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = sentence3)
            }



            item {
                HeaderText(text = sentence7)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.twelve),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = sentence8)
            }

            item {
                RegularBodyText(text = sentence9)
            }



            item {
                HeaderText(text = sentence15)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.thirteen),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = sentence16)
            }

            item {
                HeaderText(text = sentence19)
            }
            item {
                RegularBodyText(text = sentence18)
            }




            item {
                HeaderText(text = sentence12)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.fourteen),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = sentence13)
            }


            item {
                HeaderText(text = sentence20)
                RegularBodyText(text = sentence6)
            }
        }
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

@Preview
@Composable
fun InstructionsScreenPreview() {
    InstructionsScreen()
}
