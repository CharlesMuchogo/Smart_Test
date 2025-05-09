package com.charlesmuchogo.research.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.presentation.common.CenteredColumn

@Preview
@Composable
fun WelcomeContent(
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment= Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        CenteredColumn(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Faida Predict",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Transform Your Business with AI-Powered Financial Insights," +
                        "Smart forecasting for SMEs made simple",
                textAlign = TextAlign.Center,
            )
        }
    }
}