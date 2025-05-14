package com.charlesmuchogo.research.presentation.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
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
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.presentation.navigation.EditProfilePage
import com.charlesmuchogo.research.presentation.navigation.LoginPage
import com.charlesmuchogo.research.presentation.navigation.ProfilePage
import com.charlesmuchogo.research.presentation.utils.PRIVACY_POLICY_URL
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.TERMS_AND_CONDITIONS_URL
import com.charlesmuchogo.research.presentation.utils.openInAppBrowser


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(profileState.message.toString())
                }
            }

            ResultStatus.SUCCESS -> {
                profileState.data?.let {
                    ProfileListView(
                        profile = it,
                        navController = navController
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
    navController: NavController
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val context = LocalContext.current

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
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            Box(
                modifier =
                    Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.18f)
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProfileIcon(
                        image = profile.profilePhoto,
                        modifier = Modifier.clip(RoundedCornerShape(100)),
                        photoSize = 56.dp,
                    )

                    Column(
                        modifier =
                            Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            profile.firstName + " " + profile.lastName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        )
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                            text = profile.email,
                        )
                    }

                    TextButton(onClick = {
                        navController.navigate(EditProfilePage)
                    }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp), contentDescription = "Edit")
                            Text("Edit", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }

        item {

            Text(
                "General",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                modifier = Modifier.padding(bottom = 8.dp),
            )

        }

        item {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileCard(
                        modifier = Modifier,
                        label = profile.firstName + " " + profile.lastName,
                        prefixIcon = Icons.Default.Person,
                        onClick = {},
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = profile.email,
                        prefixIcon = Icons.Default.Email,
                        onClick = {},
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = profile.phone,
                        prefixIcon = Icons.Default.Phone,
                        onClick = {},
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = profile.educationLevel.ifBlank { "N/A" },
                        prefixIcon = Icons.Default.School,
                        onClick = {},
                    )
                }
            }
        }

        item {
            Text(
                "Preferences",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        item {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {


                    ProfileCard(
                        modifier = Modifier,
                        label = stringResource(R.string.darkTheme),
                        prefixIcon = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        onClick = {
                            authenticationViewModel.updateAppTheme(theme = !darkTheme)
                        },
                        trailingIcon = {
                            Switch(checked = darkTheme, onCheckedChange = {
                                authenticationViewModel.updateAppTheme(theme = !darkTheme)
                            })
                        },
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = stringResource(R.string.keepHistory),
                        prefixIcon = if (profile.hideResults) Icons.Default.Lock else Icons.Default.LockOpen,
                        onClick = {
                            authenticationViewModel.updateUser(profile.copy(hideResults = !hideResults))
                        },
                        trailingIcon = {
                            Switch(checked = !hideResults, onCheckedChange = {
                                authenticationViewModel.updateUser(profile.copy(hideResults = !hideResults))
                            })
                        },
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = stringResource(R.string.privacyPolicy),
                        prefixIcon = Icons.Default.Info,
                        onClick = {
                            openInAppBrowser(context = context, url = PRIVACY_POLICY_URL)
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                                contentDescription = null,
                            )
                        },
                    )
                    ProfileCard(
                        modifier = Modifier,
                        label = stringResource(R.string.termsAndConditions),
                        prefixIcon = Icons.Default.Security,
                        onClick = {
                            openInAppBrowser(context = context, url = TERMS_AND_CONDITIONS_URL)
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                                contentDescription = null,
                            )
                        },
                    )

                    ProfileCard(
                        modifier = Modifier,
                        label = stringResource(R.string.logOut),
                        prefixIcon = Icons.AutoMirrored.Filled.Logout,
                        onClick = {
                            showLogoutDialog = true
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
            Spacer(Modifier.padding(top = 8.dp))
        }
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
