package com.charlesmuchogo.research.presentation.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.presentation.common.AppStatusButton
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import okhttp3.internal.ws.RealWebSocket
import ui.theme.lightGreen
import ui.theme.lightYellow

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    result: TestResult,
    historyViewModel: TestResultsViewModel = hiltViewModel()
) {
    var showMoreActions by remember { mutableStateOf(false) }
    val showDeleteDialog by historyViewModel.showDeleteTestDialog.collectAsState()
    val deleteHistoryState by historyViewModel.deleteTestResultsStatus.collectAsState()
    val color  = when(result.results.uppercase()){
        "NEGATIVE" -> lightGreen
        "POSITIVE" -> MaterialTheme.colorScheme.error
        "INVALID" -> lightYellow
        else -> MaterialTheme.colorScheme.tertiary
    }

    if (showDeleteDialog) {
        AppAlertDialog(
            onDismissRequest = { historyViewModel.updateShowDeleteTestDialog(show = false) },
            onConfirmation = {
                historyViewModel.deleteTest(testResult = result)
                showMoreActions = !showMoreActions
            },
            dialogTitle = stringResource(R.string.deleteTest),
            dialogText = stringResource(R.string.cannotBeunDone),
            confirmContent = {
                when (deleteHistoryState.status) {
                    ResultStatus.SUCCESS,
                    ResultStatus.ERROR,
                    ResultStatus.INITIAL -> {
                        Text(stringResource(R.string.confirm))
                    }

                    ResultStatus.LOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(26.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(1.dp, shape = RoundedCornerShape(5))
            .clip(
                RoundedCornerShape(5)
            )
            .background(MaterialTheme.colorScheme.background)
            .clickable {

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
                        contentDescription = null
                    )

                    Text(
                        text = result.status,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                IconButton(onClick = { showMoreActions = !showMoreActions }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                }

            }

            Text(
                text = result.date,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 12.dp),
                style = MaterialTheme.typography.labelMedium,
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                AppStatusButton(
                    onClick = {},
                    label = if (result.image.isNotBlank() && result.results.isNotBlank()) result.results else "N/A",
                    containerColor = color
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            AnimatedVisibility(visible = showMoreActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { showMoreActions = !showMoreActions }) {
                        Text(
                            text = stringResource(R.string.Cancel),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    TextButton(onClick = {  historyViewModel.updateShowDeleteTestDialog(show = !showDeleteDialog) }) {
                        Text(
                            text = stringResource(R.string.Delete) ,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
