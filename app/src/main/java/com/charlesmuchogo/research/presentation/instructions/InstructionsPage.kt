package com.charlesmuchogo.research.presentation.instructions

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InstructionsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            ExoPlayerView()
        }

        item {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        item {

            // First Heading
            Text(
                text = FIRST_HEADING,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Blue.copy(blue = 0.8f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // First Instructions
            Text(
                text = FIRST_INSTRUCTIONS,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            // Second Instructions
            Text(text = SECOND_INSTRUCTIONS, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))
            Image(
                painter = painterResource(R.drawable.pdf1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            // Second Heading
            Text(
                text = SECOND_HEADING,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Step Two Instructions
            Text(text = STEP_TWO_INSTRUCTIONS, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))
            Image(
                painter = painterResource(R.drawable.pdf2),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            // Third Heading
            Text(
                text = THIRD_HEADING,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Step Three Instructions
            Text(text = STEP_THREE_INSTRUCTIONS, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))
            Image(
                painter = painterResource(R.drawable.pdf3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            // Recommendations Heading
            Text(
                text = RECOMENDATIONS_HEADING,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Blue.copy(blue = 0.8f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Recommendations
            Text(
                text = RECOMENDATIONS,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Blue.copy(blue = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            )
            Image(
                painter = painterResource(R.drawable.pdf4),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            // Important Information
            Text(text = IMPORTANT_INFORMATION, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))

            // Precautions Heading
            Text(
                text = PRECAUTIONS_HEAD,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Blue.copy(blue = 0.8f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Image(
                painter = painterResource(R.drawable.pdf5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            // Precautions
            Text(text = PRECAUTIONS, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))

            // Reading Heading
            Text(
                text = READING_HEAD,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Blue.copy(blue = 0.8f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Reading
            Text(text = READING, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))

            // Interpretation Heading
            Text(
                text = INTERPRETATION_HEADING,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Interpretation Negative
            Text(text = INTERPRETATION_NEGATIVE, style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify))

            // Positive Test Head
            Text(
                text = POSITIVE_TEST_HEAD,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }


    }
}
