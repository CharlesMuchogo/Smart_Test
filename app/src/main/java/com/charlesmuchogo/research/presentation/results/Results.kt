package com.charlesmuchogo.research.presentation.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.presentation.common.AppLoadingDialog
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import ui.theme.lightGreen
import ui.theme.lightYellow

@Composable
fun ResultsRoot(id: Long) {

    val viewModel = hiltViewModel<ResultsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.onAction(ResultsAction.OnLoadResult(id))
    }

    ResultsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    state: ResultsState,
    onAction: (ResultsAction) -> Unit,
) {

    LaunchedEffect(state.hasDeleted) {
        if(state.hasDeleted){
            navController.popBackStack()
        }
    }

    if (state.showDeleteDialog) {
        AppAlertDialog(
            onDismissRequest = { onAction(ResultsAction.OnShowDeleteDialog(!state.showDeleteDialog)) },
            onConfirmation = {
                onAction(ResultsAction.OnDeleteResults)
            },
            dialogTitle = stringResource(R.string.deleteTest),
            dialogText = stringResource(R.string.cannotBeunDone),
            confirmContent = {
                Text(stringResource(R.string.confirm))
            }
        )
    }

    AnimatedVisibility(
        visible = state.isDeleting,
        enter = fadeIn() + scaleIn(initialScale = 0.4f),
        exit = fadeOut() + scaleOut(targetScale = 0.4f),
    ) {
        AppLoadingDialog(label = "Deleting...")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.testResults),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {

                        IconButton(
                            onClick = {
                                onAction(ResultsAction.OnShowMoreChange(!state.showMore))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More"
                            )

                        }

                        DropdownMenu(
                            containerColor = MaterialTheme.colorScheme.background,
                            expanded = state.showMore,
                            onDismissRequest = { onAction(ResultsAction.OnShowMoreChange(!state.showMore)) }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Delete, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.error, contentDescription = "Delete")
                                },
                                text = { Text(stringResource(R.string.deleteTest), style = MaterialTheme.typography.labelMedium) },
                                onClick = {
                                    onAction(ResultsAction.OnShowDeleteDialog(!state.showDeleteDialog))
                                    onAction(ResultsAction.OnShowMoreChange(!state.showMore))
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Share, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onBackground, contentDescription = "Share")
                                },
                                text = { Text(stringResource(R.string.shareTest), style = MaterialTheme.typography.labelMedium) },
                                onClick = {
                                    onAction(ResultsAction.OnShowMoreChange(!state.showMore))
                                }
                            )
                        }
                    }
                }
            )
        }) { values ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
                .padding(horizontal = 16.dp)
        ) {

            when (state.result == null) {
                true -> {
                    CenteredColumn(modifier = Modifier.padding(values)) {
                        Text("Result was not found. try again later ")
                    }
                }

                false -> {

                    val color = when (state.result.results.uppercase()) {
                        "NEGATIVE" -> lightGreen
                        "POSITIVE" -> MaterialTheme.colorScheme.error
                        "INVALID" -> lightYellow
                        else -> MaterialTheme.colorScheme.tertiary
                    }

                    LazyColumn {
                        item {
                            ResultDetailCard(
                                title = "Date",
                                icon = Icons.Default.CalendarMonth,
                                description = state.result.date
                            )
                        }
                        item {
                            ResultDetailCard(
                                title = "Status",
                                icon = Icons.Default.History,
                                description = state.result.status
                            )
                        }
                        item {
                            ResultDetailCard(
                                title = "Care option",
                                icon = Icons.Default.LocalHospital,
                                description = state.result.care_option.ifBlank { "N/A" }
                            )
                        }
                        item {
                            ResultDetailCard(
                                title = "Result",
                                icon = Icons.Default.Person,
                                description = state.result.results,
                                color = color
                            )
                        }

                        if (state.result.partnerResults.isNotBlank()) {
                            item {
                                ResultDetailCard(
                                    title = "Partner result",
                                    icon = Icons.Default.People,
                                    description = state.result.partnerResults
                                )
                            }
                        }

                        item {
                            Spacer(Modifier.height(24.dp))
                        }

                        if (state.result.reason.isNotBlank()) {
                            item {

                                Text(
                                    "Reason",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                                )

                            }

                            item {
                                Text(
                                    text = state.result.reason,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ResultsScreenPreview() {

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
        ResultsScreen(
            state = ResultsState(result = testResult),
            onAction = {},
        )
    }
}




