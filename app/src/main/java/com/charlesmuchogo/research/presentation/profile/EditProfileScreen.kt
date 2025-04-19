package com.charlesmuchogo.research.presentation.profile

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.domain.actions.UpdateProfileAction
import com.charlesmuchogo.research.domain.models.TextFieldState
import com.charlesmuchogo.research.domain.viewmodels.EditProfileViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.common.AppButton
import com.charlesmuchogo.research.presentation.common.AppDropDown
import com.charlesmuchogo.research.presentation.common.AppImagePickerDialog
import com.charlesmuchogo.research.presentation.common.AppLoginButtonContent
import com.charlesmuchogo.research.presentation.common.AppTextField
import com.charlesmuchogo.research.presentation.utils.ImagePicker
import com.charlesmuchogo.research.presentation.utils.levelsOfEducation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {

    val profileViewModel = hiltViewModel<EditProfileViewModel>()
    val pageState by profileViewModel.pageState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val imagePicker = ImagePicker(context)

    LaunchedEffect(pageState.hasSubmitted) {
        if(pageState.hasSubmitted){
            navController.popBackStack()
        }
    }


    imagePicker.RegisterPicker(onImagePicked = { image ->
        profileViewModel.onAction(UpdateProfileAction.OnImageChange(image))
    })

    AnimatedVisibility(visible = pageState.showImagePicker) {
        AppImagePickerDialog(
            onPickImage = {
                profileViewModel.onAction(UpdateProfileAction.OnShowImagePicker(false))
                imagePicker.pickImage()
            },
            onDismiss = {
                profileViewModel.onAction(UpdateProfileAction.OnShowImagePicker(false))
            },
            onCaptureImage = {
                profileViewModel.onAction(UpdateProfileAction.OnShowImagePicker(false))
                imagePicker.captureImage()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
                    }
                },
                title = {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleMedium
                    )
                })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Box(
                        modifier =
                            Modifier.size(180.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(160.dp)
                                    .clip(shape = CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .border(
                                        border =
                                            BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.secondaryContainer,
                                            ),
                                        shape = CircleShape,
                                    ),
                        ) {
                            if (pageState.image != null) {
                                BitmapFactory.decodeByteArray(
                                    pageState.image,
                                    0,
                                    pageState.image!!.size
                                )
                                    ?.let { img ->
                                        Image(
                                            bitmap = img.asImageBitmap(),
                                            contentDescription = "profile image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                            } else {
                                ProfileIcon(
                                    image = pageState.imageLink,
                                    modifier = Modifier.fillMaxSize(),
                                    iconSize = 45.dp,
                                    icon = Icons.Default.Person,
                                    onclick = {}
                                )
                            }
                        }

                        IconButton(

                            onClick = {
                                profileViewModel.onAction(UpdateProfileAction.OnShowImagePicker(true))
                            },
                            colors =
                                IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                ),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 4.dp),
                        ) {
                            Icon(
                                imageVector = if (!pageState.imageLink.isNullOrBlank() || pageState.image != null) Icons.Default.Edit else Icons.Default.Add,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }

                item {
                    AppTextField(
                        required = true,
                        label = "First name",
                        value = pageState.firstName,
                        placeholder = "John",
                        error = pageState.firstNameError,
                        onValueChanged = { string ->
                            profileViewModel.onAction(
                                action =
                                    UpdateProfileAction.OnFirstNameChange(
                                        string,
                                    ),
                            )
                        },
                        keyboardType = KeyboardType.Email,
                    )
                }

                item {
                    AppTextField(
                        required = true,
                        label = "Last name",
                        value = pageState.lastName,
                        placeholder = "Doe",
                        error = pageState.lastNameError,
                        onValueChanged = { string ->
                            profileViewModel.onAction(
                                action =
                                    UpdateProfileAction.OnLastNameChange(
                                        string,
                                    ),
                            )
                        },
                        keyboardType = KeyboardType.Email,
                    )
                }

                item {
                    AppTextField(
                        required = true,
                        label = "Phone Number",
                        value = pageState.phoneNumber,
                        placeholder = "0712345678",
                        error = pageState.phoneNumberError,
                        onValueChanged = { string ->
                            profileViewModel.onAction(
                                action =
                                    UpdateProfileAction.OnPhoneNumberChange(
                                        string,
                                    ),
                            )
                        },
                        keyboardType = KeyboardType.Phone,
                    )
                }

                item {
                    AppDropDown(
                        options = levelsOfEducation,
                        label = {
                            Text(
                                modifier = Modifier.padding(vertical = 4.dp),
                                text = "What is your education level?",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            )
                        },
                        selectedOption = TextFieldState(
                            text = pageState.educationLevel.ifBlank { "Select your education level" },
                            isSelected = pageState.educationLevel.isNotBlank(),
                            error = pageState.educationLevelError
                        ),
                        onOptionSelected = {
                            profileViewModel.onAction(UpdateProfileAction.OnEducationLevelChange(it))
                        }) {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }


                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = pageState.testedBefore,
                            onCheckedChange = { isChecked ->
                                profileViewModel.onAction(
                                    UpdateProfileAction.OnHasTestedBeforeChange(isChecked)
                                )
                            },
                        )
                        Text(
                            "I have tested my HIV status before",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }


                item {
                    Spacer(Modifier.height(56.dp))
                }
            }


            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = MaterialTheme.shapes.extraLarge,
                onClick = {
                    profileViewModel.onAction(UpdateProfileAction.OnSubmit)
                },
                content = {
                    when (pageState.isSubmitting) {
                        false -> {
                            Text("Save")
                        }
                        true -> {
                            AppLoginButtonContent(message = "Saving...")
                        }
                    }
                },
            )
        }
    }
}