package com.charlesmuchogo.research.presentation.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.BannerAd
import com.charlesmuchogo.research.ads.BannerStyle
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ChatPage
import com.charlesmuchogo.research.navigation.ClinicsPage
import com.charlesmuchogo.research.navigation.CommunityPage
import com.charlesmuchogo.research.navigation.DoctorsPage
import com.charlesmuchogo.research.navigation.InstructionsPage
import com.charlesmuchogo.research.navigation.PendingTestPage
import com.charlesmuchogo.research.navigation.TestPage
import com.charlesmuchogo.research.navigation.TestResultsPage
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.presentation.utils.RequestPermissions
import com.charlesmuchogo.research.ui.theme.extendedColors
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeScreen() {
    RequestPermissions()
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeDashboard(state = state)
}

fun startTest(hasPendingResults: Boolean) {
    if (hasPendingResults) navController.navigate(PendingTestPage) else navController.navigate(TestPage)
}

@Composable
fun HomeDashboard(state: HomeState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Greeting(firstName = state.firstName)

        NextTestCard(onStart = { startTest(state.hasPendingResults) })

        state.latestResult?.let { result ->
            LatestResultCard(result = result, onClick = { navController.navigate(TestResultsPage(result.id)) })
        }

        Text(
            text = stringResource(R.string.get_support),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SupportTile(
                    title = stringResource(R.string.talk_to_doctor),
                    subtitle = stringResource(R.string.talk_to_doctor_hint),
                    icon = Icons.Outlined.MedicalServices,
                    container = MaterialTheme.colorScheme.tertiaryContainer,
                    content = MaterialTheme.colorScheme.onTertiaryContainer,
                    onClick = { navController.navigate(DoctorsPage) },
                    modifier = Modifier.weight(1f),
                )
                SupportTile(
                    title = stringResource(R.string.community),
                    subtitle = stringResource(R.string.community_hint),
                    icon = Icons.Outlined.Groups,
                    container = MaterialTheme.extendedColors.coralContainer,
                    content = MaterialTheme.extendedColors.onCoralContainer,
                    onClick = { navController.navigate(CommunityPage) },
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SupportTile(
                    title = stringResource(R.string.find_clinic),
                    subtitle = stringResource(R.string.find_clinic_hint),
                    icon = Icons.Outlined.LocationOn,
                    container = MaterialTheme.colorScheme.primaryContainer,
                    content = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { navController.navigate(ClinicsPage) },
                    modifier = Modifier.weight(1f),
                )
                SupportTile(
                    title = stringResource(R.string.smart_assistant),
                    subtitle = stringResource(R.string.smart_assistant_hint),
                    icon = Icons.Outlined.AutoAwesome,
                    container = MaterialTheme.extendedColors.amberContainer,
                    content = MaterialTheme.extendedColors.onAmberContainer,
                    onClick = { navController.navigate(ChatPage) },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        BannerAd(style = BannerStyle.INLINE)

        Spacer(Modifier.size(12.dp))
    }
}

@Composable
private fun Greeting(firstName: String?) {
    val hour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    val salutation = when {
        hour < 12 -> stringResource(R.string.good_morning)
        hour < 17 -> stringResource(R.string.good_afternoon)
        else -> stringResource(R.string.good_evening)
    }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = salutation,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = firstName?.let { stringResource(R.string.greeting_name, it) } ?: stringResource(R.string.greeting_guest),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
        )
    }
}

@Composable
private fun NextTestCard(onStart: () -> Unit) {
    val heroBackground = Color(0xFF2E2A8F)
    val heroAccent = Color(0xFF5A55D6)
    val onHero = Color.White
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(heroBackground),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(onHero.copy(alpha = 0.07f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 22.dp, end = 22.dp)
                .size(56.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(heroAccent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Science, contentDescription = null, tint = onHero, modifier = Modifier.size(28.dp))
        }
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.self_test_label).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = onHero.copy(alpha = 0.75f),
            )
            Text(
                text = stringResource(R.string.next_test_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = onHero,
                modifier = Modifier.padding(end = 72.dp),
            )
            Text(
                text = stringResource(R.string.next_test_body),
                style = MaterialTheme.typography.bodyMedium,
                color = onHero.copy(alpha = 0.85f),
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = onHero, contentColor = heroBackground),
                ) {
                    Text(stringResource(R.string.start_a_test), fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { navController.navigate(InstructionsPage) },
                    modifier = Modifier.heightIn(min = 48.dp),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, onHero.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = onHero),
                ) {
                    Text(stringResource(R.string.how_it_works))
                }
            }
        }
    }
}

@Composable
private fun LatestResultCard(result: TestResult, onClick: () -> Unit) {
    val outcome = result.results.uppercase()
    val isPending = result.status.lowercase() == "pending" || result.results.isBlank()
    val (icon, container, content) = when {
        isPending -> Triple(Icons.Outlined.Schedule, MaterialTheme.extendedColors.amberContainer, MaterialTheme.extendedColors.onAmberContainer)
        outcome == "NEGATIVE" -> Triple(Icons.Outlined.VerifiedUser, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
        else -> Triple(Icons.Outlined.HealthAndSafety, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
    }
    SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(container),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = content)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = stringResource(R.string.last_result, result.date),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (isPending) stringResource(R.string.result_pending) else result.results.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SupportTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    container: Color,
    content: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SurfaceCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(container),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(22.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
