package com.charlesmuchogo.research.presentation.history

import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.domain.viewmodels.TestResultsViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.DoctorsPage
import com.charlesmuchogo.research.navigation.TestResultsPage
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.messages.components.FilterChipRow
import com.charlesmuchogo.research.presentation.messages.components.Pill
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.ui.theme.extendedColors

private data class ResultAppearance(val icon: ImageVector, val container: Color, val content: Color)

fun TestResult.isPendingReview(): Boolean = status.lowercase() == "pending" || results.isBlank()

fun TestResult.isCoupleTest(): Boolean = partnerImage.isNotBlank() || partnerResults.isNotBlank()

@Composable
private fun appearanceOf(result: TestResult): ResultAppearance = when {
    result.isPendingReview() -> ResultAppearance(
        Icons.Outlined.Schedule,
        MaterialTheme.extendedColors.amberContainer,
        MaterialTheme.extendedColors.onAmberContainer,
    )
    result.results.uppercase() == "NEGATIVE" -> ResultAppearance(
        Icons.Outlined.VerifiedUser,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.onTertiaryContainer,
    )
    else -> ResultAppearance(
        Icons.Outlined.HealthAndSafety,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
private fun outcomeLabel(result: TestResult): String =
    if (result.isPendingReview()) stringResource(R.string.result_pending)
    else result.results.lowercase().replaceFirstChar { it.uppercase() }

@Composable
private fun typeLabel(result: TestResult): String =
    stringResource(if (result.isCoupleTest()) R.string.with_my_partner else R.string.just_me)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    val testResultsViewModel = hiltViewModel<TestResultsViewModel>()

    val testResultsStatus =
        testResultsViewModel.testResultsStatus.collectAsStateWithLifecycle().value
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val user = authenticationViewModel.profileStatus.collectAsStateWithLifecycle().value
    val hideResults = user.data?.hideResults != false

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.my_results),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            when (testResultsStatus.status) {
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    CenteredColumn(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        AppListLoading()
                    }
                }

                ResultStatus.SUCCESS -> {
                    if (testResultsStatus.data.isNullOrEmpty() || hideResults) {
                        CenteredColumn(modifier = Modifier.weight(1f).padding(horizontal = 32.dp)) {
                            Text(
                                text = stringResource(R.string.noResults),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        TestResultsListView(modifier = Modifier.weight(1f), results = testResultsStatus.data)
                    }
                }

                ResultStatus.ERROR -> {
                    AppErrorScreen(
                        modifier = Modifier.weight(1f),
                        errorDescription = testResultsStatus.message,
                        onRetry = { testResultsViewModel.retryTestResults() },
                    )
                }
            }
        }
    }
}

@Composable
fun TestResultsListView(
    modifier: Modifier = Modifier,
    results: List<TestResult>,
) {
    var filter by rememberSaveable { mutableIntStateOf(0) }
    val latest = results.first()
    val history = when (filter) {
        1 -> results.filter { it.isPendingReview() }
        2 -> results.filter { it.isCoupleTest() }
        else -> results
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item { LatestResultCard(result = latest) }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.history),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item {
            FilterChipRow(
                labels = listOf(
                    stringResource(R.string.filter_all),
                    stringResource(R.string.filter_pending),
                    stringResource(R.string.filter_couple),
                ),
                selectedIndex = filter,
                onSelect = { filter = it },
            )
        }

        if (history.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.noResults),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
        }

        items(history, key = { it.id }) { result ->
            HistoryRow(result = result, onClick = { navController.navigate(TestResultsPage(result.id)) })
        }

        item { PrivacyNote() }

        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
private fun LatestResultCard(result: TestResult) {
    val appearance = appearanceOf(result)
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate(TestResultsPage(result.id)) },
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(appearance.container),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(appearance.icon, contentDescription = null, tint = appearance.content, modifier = Modifier.size(28.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.latest_result),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = outcomeLabel(result),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultFact(label = stringResource(R.string.tested), value = result.date, modifier = Modifier.weight(1f))
                    ResultFact(
                        label = stringResource(R.string.reviewed_by),
                        value = result.care_option.ifBlank { stringResource(R.string.not_reviewed) },
                        modifier = Modifier.weight(1f),
                    )
                }
                ResultFact(label = stringResource(R.string.test_type), value = typeLabel(result))
            }

            Button(
                onClick = { navController.navigate(DoctorsPage) },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
            ) {
                Icon(Icons.Outlined.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    text = stringResource(R.string.discuss_with_doctor),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun ResultFact(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun HistoryRow(result: TestResult, onClick: () -> Unit) {
    val appearance = appearanceOf(result)
    SurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(result.id.toString()),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(appearance.container),
                contentAlignment = Alignment.Center,
            ) {
                Icon(appearance.icon, contentDescription = null, tint = appearance.content, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = outcomeLabel(result),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = "${result.date} · ${typeLabel(result)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (result.isPendingReview()) {
                Pill(
                    text = stringResource(R.string.filter_pending),
                    container = MaterialTheme.extendedColors.amberContainer,
                    content = MaterialTheme.extendedColors.onAmberContainer,
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PrivacyNote() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(R.string.results_privacy_note),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
