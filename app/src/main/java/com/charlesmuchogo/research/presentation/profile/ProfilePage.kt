package com.charlesmuchogo.research.presentation.profile

import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.semantics.Role
import com.charlesmuchogo.research.domain.models.chat.MessagePrivacy
import com.charlesmuchogo.research.presentation.messages.components.Avatar
import com.charlesmuchogo.research.presentation.messages.components.AvatarColors
import com.charlesmuchogo.research.presentation.messages.components.SegmentedTabs
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.ui.theme.extendedColors
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.rememberAdsManager
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.navigation.EditProfilePage
import com.charlesmuchogo.research.navigation.BlockedUsersPage
import com.charlesmuchogo.research.navigation.CommunityPage
import com.charlesmuchogo.research.navigation.HistoryPage
import com.charlesmuchogo.research.navigation.LanguagePage
import com.charlesmuchogo.research.navigation.LoginPage
import com.charlesmuchogo.research.navigation.ProfilePage
import com.charlesmuchogo.research.navigation.SubscribePage
import com.charlesmuchogo.research.presentation.utils.PRIVACY_POLICY_URL
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.TERMS_AND_CONDITIONS_URL
import com.charlesmuchogo.research.presentation.utils.findActivity
import com.charlesmuchogo.research.presentation.utils.openInAppBrowser


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val profileViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileState = profileViewModel.profileStatus.collectAsStateWithLifecycle().value

    Box(
        modifier =
            modifier
                .fillMaxSize()
    ) {
        when (profileState.status) {
            ResultStatus.INITIAL, ResultStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            ResultStatus.ERROR -> {
                AppErrorScreen(
                    errorDescription = profileState.message,
                    onRetry = { profileViewModel.getCurrentUser() },
                )
            }

            ResultStatus.SUCCESS -> {
                profileState.data?.let {
                    ProfileListView(
                        profile = it,
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileListView(
    profile: User,
    modifier: Modifier = Modifier,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val identityViewModel = hiltViewModel<ProfileIdentityViewModel>()
    val identity by identityViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val adsManager = rememberAdsManager()
    val privacyOptionsRequired by adsManager.privacyOptionsRequired.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { identityViewModel.refresh() }

    val darkThemeFlow by authenticationViewModel.appTheme.collectAsStateWithLifecycle()

    val darkTheme = when (darkThemeFlow) {
        0 -> false
        1 -> true
        else -> isSystemInDarkTheme()
    }

    val hideResults = profile.hideResults

    if (showLogoutDialog) {
        AppAlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmation = {
                showLogoutDialog = false
                authenticationViewModel.logout()
                navController.navigate(LoginPage) {
                    popUpTo(ProfilePage) {
                        inclusive = true
                    }
                }
            },
            dialogTitle = stringResource(R.string.logOut),
            dialogText = stringResource(R.string.logOutDescription),
            icon = Icons.Default.Info,
        )
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(EditProfilePage) }) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    ProfileIcon(
                        image = profile.profilePhoto,
                        modifier = Modifier.clip(RoundedCornerShape(100)),
                        photoSize = 56.dp,
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${profile.firstName} ${profile.lastName}".trim(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        )
                        Text(
                            text = profile.email,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    OutlinedButton(
                        onClick = { navController.navigate(EditProfilePage) },
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(stringResource(R.string.edit), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item { SectionTitle(stringResource(R.string.community_identity)) }

        item {
            SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val alias = identity.profile?.alias.orEmpty()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Avatar(
                            name = alias.ifBlank { "?" },
                            colors = AvatarColors(MaterialTheme.extendedColors.coralContainer, MaterialTheme.extendedColors.onCoralContainer),
                            size = 44.dp,
                        )
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = alias.ifBlank { stringResource(R.string.community_name) },
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            )
                            Text(
                                text = stringResource(R.string.community_name_hint),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        TextButton(onClick = { navController.navigate(CommunityPage) }) {
                            Text(stringResource(R.string.change), fontWeight = FontWeight.Bold)
                        }
                    }
                    identity.profile?.let { community ->
                        val options = listOf(MessagePrivacy.DOCTORS_ONLY, MessagePrivacy.CIRCLES, MessagePrivacy.NONE)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.who_can_message),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            )
                            SegmentedTabs(
                                labels = listOf(
                                    stringResource(R.string.privacy_doctors),
                                    stringResource(R.string.privacy_circles),
                                    stringResource(R.string.privacy_none),
                                ),
                                selectedIndex = options.indexOf(community.messagePrivacy).coerceAtLeast(0),
                                onSelect = { identityViewModel.updatePrivacy(options[it]) },
                            )
                        }
                    }
                }
            }
        }

        item { SectionTitle(stringResource(R.string.privacy_security)) }

        item {
            SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    ToggleRow(
                        label = stringResource(R.string.keepHistory),
                        hint = stringResource(R.string.keep_history_hint),
                        icon = if (hideResults) Icons.Default.Lock else Icons.Default.LockOpen,
                        checked = !hideResults,
                        onToggle = { authenticationViewModel.updateUser(profile.copy(hideResults = !hideResults)) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ToggleRow(
                        label = stringResource(R.string.darkTheme),
                        hint = stringResource(R.string.dark_theme_hint),
                        icon = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        checked = darkTheme,
                        onToggle = { authenticationViewModel.updateAppTheme(theme = !darkTheme) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LinkRow(label = stringResource(R.string.blocked_people), icon = Icons.Default.Block) {
                        navController.navigate(BlockedUsersPage)
                    }
                }
            }
        }

        item { SectionTitle(stringResource(R.string.general)) }

        item {
            SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    LinkRow(label = stringResource(R.string.my_results), icon = Icons.Default.Schedule) {
                        navController.navigate(HistoryPage)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LinkRow(label = stringResource(R.string.subscribe), icon = Icons.Default.Payments) {
                        navController.navigate(SubscribePage)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LinkRow(label = stringResource(R.string.language), icon = Icons.Default.Public) {
                        navController.navigate(LanguagePage)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LinkRow(label = stringResource(R.string.privacyPolicy), icon = Icons.Default.Info) {
                        openInAppBrowser(context = context, url = PRIVACY_POLICY_URL)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LinkRow(label = stringResource(R.string.termsAndConditions), icon = Icons.Default.Security) {
                        openInAppBrowser(context = context, url = TERMS_AND_CONDITIONS_URL)
                    }
                    if (privacyOptionsRequired) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        LinkRow(label = stringResource(R.string.ad_privacy_choices), icon = Icons.Default.Tune) {
                            context.findActivity()?.let { adsManager.showPrivacyOptions(it) }
                        }
                    }
                }
            }
        }

        item {
            SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = { showLogoutDialog = true }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = stringResource(R.string.logOut),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
        modifier = Modifier.padding(top = 8.dp),
    )
}

@Composable
private fun ToggleRow(
    label: String,
    hint: String,
    icon: ImageVector,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = { onToggle() })
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(
                text = hint,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun LinkRow(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    prefixIcon: ImageVector,
    label: String,
    trailingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable(
                    onClick = { onClick.invoke() },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = prefixIcon, contentDescription = label)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
            }

            trailingIcon()
        }
    }
}
