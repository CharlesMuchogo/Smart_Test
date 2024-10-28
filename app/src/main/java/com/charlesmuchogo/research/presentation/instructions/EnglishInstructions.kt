package com.charlesmuchogo.research.presentation.instructions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.ExoPlayerView
import com.charlesmuchogo.research.presentation.navigation.TestPage

@Composable
fun EnglishInstructions(modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                    text = ENGLISH_FIRST_HEADING, style = MaterialTheme.typography.bodyLarge
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
                    text = englishSentence1,
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
                RegularBodyText(text = englishSentence2)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.three),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = englishSentence21)
            }


            item {
                Image(
                    painter = painterResource(R.drawable.four),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = englishSentence22)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.five),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = englishSentence23)
            }


            item {
                Image(
                    painter = painterResource(R.drawable.six),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence27)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.seven),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence28)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.eight),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence26)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.nine),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence24)
            }



            item {
                Image(
                    painter = painterResource(R.drawable.ten),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence25)
            }




            item {
                Image(
                    painter = painterResource(R.drawable.eleven),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = englishSentence3)
            }



            item {
                HeaderText(text = englishSentence7)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.twelve),
                    contentDescription = "Image"
                )
            }

            item {
                RegularBodyText(text = englishSentence8)
            }

            item {
                RegularBodyText(text = englishSentence9)
            }



            item {
                HeaderText(text = englishSentence15)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.thirteen),
                    contentDescription = "Image"
                )
            }



            item {
                RegularBodyText(text = englishSentence16)
            }

            item {
                HeaderText(text = englishSentence19)
            }
            item {
                RegularBodyText(text = englishSentence18)
            }




            item {
                HeaderText(text = englishSentence12)
            }

            item {
                Image(
                    painter = painterResource(R.drawable.fourteen),
                    contentDescription = "Image"
                )
            }


            item {
                RegularBodyText(text = englishSentence13)
            }


            item {
                HeaderText(text = englishSentence20)
                RegularBodyText(text = englishSentence6)
            }

            item {
                AppButton(onClick = onClick) {
                    Text("Take a test")
                }
            }
        }
    }
}