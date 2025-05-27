package com.charlesmuchogo.research.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.presentation.common.AppStatusButton
import com.charlesmuchogo.research.presentation.results.ResultsScreen
import com.charlesmuchogo.research.presentation.results.ResultsState
import ui.theme.lightGreen
import ui.theme.lightYellow

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    result: TestResult,
    onClick: (TestResult) -> Unit,
) {

    val color = when (result.results.uppercase()) {
        "NEGATIVE" -> lightGreen
        "POSITIVE" -> MaterialTheme.colorScheme.error
        "INVALID" -> lightYellow
        else -> MaterialTheme.colorScheme.tertiary
    }

    Box(
        modifier = modifier
            .fillMaxWidth().height(180.dp)
            .padding(vertical = 4.dp)
            .clip(
                MaterialTheme.shapes.small
            )
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick(result)
            }
            .testTag(result.id.toString())
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        contentDescription = null
                    )

                    Text(
                        text = result.status,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                AppStatusButton(
                    onClick = {},
                    label = if (result.image.isNotBlank() && result.results.isNotBlank()) result.results else "N/A",
                    containerColor = color
                )

            }

            Text(
                text = result.date,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    contentDescription = null
                )

                Text(
                    text = result.care_option.ifBlank { "No care option selected" },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview()
@Composable
fun HistoryCardPreview() {
    val testResult = TestResult(
        id = 1,
        care_option = "Kenyatta Hospital",
        uuid = "asjasjjasjasjjjas",
        date = "12-05-2025",
        image = "",
        status = "Pending",
        partnerImage = "",
        partnerResults = "",
        results = "Invalid",
        reason = "Results are invalid. Read the instructions, use a test equipment to take a test and upload the results to get your correct results",
        userId = 1
    )

    Surface(color = MaterialTheme.colorScheme.background) {
        HistoryCard(
            result =  testResult,
            onClick = {},
        )
    }
}
