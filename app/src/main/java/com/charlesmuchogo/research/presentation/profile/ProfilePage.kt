package com.charlesmuchogo.research.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.authentication.LoginPage
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.presentation.utils.LocalAppNavigator
import com.charlesmuchogo.research.presentation.utils.ResultStatus

class ProfilePage : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalAppNavigator.currentOrThrow
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                        }
                    },
                    title = {
                        Text("Profile")
                    }
                )
            }
        ) {
            ProfileScreen(modifier = Modifier.padding(it))
        }

    }

}


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val profileViewModel = hiltViewModel<AuthenticationViewModel>()
    val profileState = profileViewModel.profileStatus.collectAsStateWithLifecycle().value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
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
                profileState.data?.let { ProfileListView(profile = it) }
            }
        }

    }
}

@Composable
fun ProfileListView(profile: User, modifier: Modifier = Modifier) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val navigator = LocalAppNavigator.currentOrThrow
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()

    val darkTheme =  false



    if (showLogoutDialog) {

        AppAlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmation = {
                showLogoutDialog = false
                authenticationViewModel.logout()
                navigator.replaceAll(LoginPage())
            },
            dialogTitle = "Log out",
            dialogText = "You are about to log out",
            icon = Icons.Default.Info
        )
    }
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.18f)
                    .shadow(1.dp, shape = RoundedCornerShape(4))
                    .clip(
                        RoundedCornerShape(4)
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProfileIcon(
                        image = profile.profilePhoto,
                        modifier = Modifier.clip(RoundedCornerShape(100)),
                        photoSize = 70.dp,
                        onclick = {}
                    )

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            profile.firstName + " "+ profile.lastName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                            text = profile.email
                        )
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "General",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }


            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, shape = RoundedCornerShape(4))
                    .clip(
                        RoundedCornerShape(4)
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileCard(
                        modifier = Modifier,
                        label = profile.firstName + " " + profile.lastName,
                        prefixIcon = Icons.Default.Person,
                        onClick = {})

                    ProfileCard(
                        modifier = Modifier,
                        label = profile.email,
                        prefixIcon = Icons.Default.Email,
                        onClick = {})

                        ProfileCard(
                            modifier = Modifier,
                            label = profile.phone,
                            prefixIcon = Icons.Default.Phone,
                            onClick = {})


                        ProfileCard(
                            modifier = Modifier,
                            label = profile.educationLevel.ifBlank{ "N/A" },
                            prefixIcon = Icons.Default.School,
                            onClick = {})



                }
            }


        }

        item {
            Text(
                "Preferences",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, shape = RoundedCornerShape(4))
                    .clip(
                        RoundedCornerShape(4)
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileCard(
                        modifier = Modifier,
                        label = "Dark Theme",
                        prefixIcon = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        onClick = {  },
                        trailingIcon = {
                            Switch(checked = darkTheme, onCheckedChange = {

                            })
                        })



                    ProfileCard(
                        modifier = Modifier,
                        label = "Log Out",
                        prefixIcon = Icons.AutoMirrored.Filled.Logout,
                        onClick = {
                            showLogoutDialog = true
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                                contentDescription = null,
                            )
                        }
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
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(
                onClick = { onClick.invoke() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = prefixIcon, contentDescription = label)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = label)
            }

            trailingIcon()
        }
    }
}

