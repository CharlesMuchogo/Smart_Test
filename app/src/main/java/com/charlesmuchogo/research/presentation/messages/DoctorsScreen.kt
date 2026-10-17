package com.charlesmuchogo.research.presentation.messages

import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.domain.models.chat.Doctor
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ConversationPage
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.FilterChipRow
import com.charlesmuchogo.research.presentation.messages.components.Pill
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.presentation.messages.components.VerifiedBadge
import com.charlesmuchogo.research.presentation.messages.components.conversationColors
import com.charlesmuchogo.research.ui.theme.extendedColors

@Composable
fun DoctorsRoot() {
    val viewModel = hiltViewModel<DoctorsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.openConversation.collect { id ->
            navController.navigate(ConversationPage(id))
        }
    }

    AuthControllerScreen(showTopBar = true) {
        DoctorsScreen(state = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsScreen(state: DoctorsState, onAction: (DoctorsAction) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.talk_to_doctor),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    )
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { VerifiedBanner() }

            if (state.specialties.size > 1) {
                item {
                    val labels = listOf(stringResource(R.string.filter_all)) + state.specialties
                    val selectedIndex = state.selectedSpecialty?.let { state.specialties.indexOf(it) + 1 } ?: 0
                    FilterChipRow(
                        labels = labels,
                        selectedIndex = selectedIndex,
                        onSelect = { index ->
                            onAction(DoctorsAction.OnSpecialtySelected(if (index == 0) null else state.specialties[index - 1]))
                        },
                    )
                }
            }

            when {
                state.isLoading -> item { AppListLoading(modifier = Modifier.height(240.dp)) }

                state.error != null -> item {
                    AppErrorScreen(errorDescription = state.error, onRetry = { onAction(DoctorsAction.Load) })
                }

                state.visibleDoctors.isEmpty() -> item {
                    Text(
                        text = stringResource(R.string.no_doctors),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    )
                }

                else -> items(state.visibleDoctors, key = { it.userId }) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        isStarting = state.startingDoctorId == doctor.userId,
                        onStartChat = { onAction(DoctorsAction.OnStartChat(doctor)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun VerifiedBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.VerifiedUser,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.size(28.dp),
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = stringResource(R.string.verified_clinicians),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onTertiary,
            )
            Text(
                text = stringResource(R.string.verified_clinicians_body),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
private fun DoctorCard(doctor: Doctor, isStarting: Boolean, onStartChat: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(
                    name = doctor.name,
                    colors = conversationColors(ConversationType.DOCTOR),
                    size = 52.dp,
                    online = doctor.available,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = doctor.displayName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        )
                        if (doctor.verified) {
                            Spacer(Modifier.width(4.dp))
                            VerifiedBadge()
                        }
                    }
                    if (doctor.specialty.isNotBlank()) {
                        Text(
                            text = doctor.specialty,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (doctor.languages.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.speaks, doctor.languages),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Pill(
                    text = stringResource(if (doctor.available) R.string.online else R.string.away),
                    container = if (doctor.available) MaterialTheme.extendedColors.successContainer else MaterialTheme.colorScheme.surfaceVariant,
                    content = if (doctor.available) MaterialTheme.extendedColors.onSuccessContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Top),
                )
            }
            if (doctor.bio.isNotBlank()) {
                Text(
                    text = doctor.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                )
            }
            Button(
                onClick = onStartChat,
                enabled = !isStarting,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 46.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                if (isStarting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.surface,
                    )
                } else {
                    Text(stringResource(R.string.start_chat), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
