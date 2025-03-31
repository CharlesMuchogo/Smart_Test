package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.actions.UpdateProfileAction
import com.charlesmuchogo.research.domain.states.UpdateProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject
constructor(
    private val database: AppDatabase,
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    val pageState = MutableStateFlow(UpdateProfileState())

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {

            database
                .userDao()
                .getUser()
                .catch {
                    it.printStackTrace()
                }.collect {
                    it?.let { user ->
                        pageState.value =
                            pageState.value.copy(
                                dateOfBirth = user.age,
                                firstName = user.firstName,
                                lastName = user.lastName,
                                phoneNumber = user.phone,
                                gender = user.gender,
                                educationLevel = user.educationLevel,
                                testedBefore = user.testedBefore,
                            )
                    }
                }
        }
    }

    fun onAction(action: UpdateProfileAction) {
        when (action) {
            is UpdateProfileAction.OnAgeChange -> {
                pageState.value = pageState.value.copy(dateOfBirth = action.value)
            }

            is UpdateProfileAction.OnConfirmPasswordChange -> {
                pageState.value = pageState.value.copy(dateOfBirth = action.value)

            }

            is UpdateProfileAction.OnEducationLevelChange -> {
                pageState.value = pageState.value.copy(educationLevel = action.value)

            }

            is UpdateProfileAction.OnFirstNameChange -> {
                pageState.value = pageState.value.copy(firstName = action.value)

            }

            is UpdateProfileAction.OnGenderChange -> {
                pageState.value = pageState.value.copy(genderError = action.value)

            }

            is UpdateProfileAction.OnHasTestedBeforeChange -> {
                pageState.value = pageState.value.copy(testedBefore = action.value)

            }

            is UpdateProfileAction.OnImageChange -> {
                pageState.value = pageState.value.copy(image = action.value)

            }

            is UpdateProfileAction.OnLastNameChange -> {
                pageState.value = pageState.value.copy(lastName = action.value)

            }

            is UpdateProfileAction.OnMaritalStatusChange -> {
                //pageState.value = pageState.value.copy(= action. value)

            }

            is UpdateProfileAction.OnPhoneNumberChange -> {
                pageState.value = pageState.value.copy(phoneNumber = action.value)

            }

            UpdateProfileAction.OnSubmit -> {

            }

            is UpdateProfileAction.OnShowImagePicker -> {
                pageState.value = pageState.value.copy(showImagePicker = action.value)
            }
        }

    }
}