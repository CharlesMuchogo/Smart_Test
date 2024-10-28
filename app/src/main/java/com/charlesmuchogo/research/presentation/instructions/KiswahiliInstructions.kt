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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.ExoPlayerView

@Composable
fun KiswahiliInstructions(modifier: Modifier = Modifier, onClick: () -> Unit) {
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

            item {
                //navController.navigate(TestPage)
                AppButton(onClick = onClick) {
                    Text("Take a test")
                }
            }
        }
    }
}