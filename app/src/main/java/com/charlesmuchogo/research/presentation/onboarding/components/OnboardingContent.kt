package com.charlesmuchogo.research.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.CenteredColumn

@Preview
@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier,
    title: String = "Real-Time Insights",
    text: String = "Make data-driven decisions, " +
            "Monitor key financial KPIs in real-time, " +
            "Track cash flow and profitability instantly, "+
            "Get automated alerts for unusual transactions",
    painter: Painter = painterResource(R.drawable.learnmore)
){
    CenteredColumn(
        columnVerticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
    ) {


        Image(
            painter = painter,
            contentDescription = "Statistics Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(240.dp)
        )

        Text(
            text = title ,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = text ,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
