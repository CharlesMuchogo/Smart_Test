package com.charlesmuchogo.research.domain.actions

sealed interface UpdateProfileAction {

    data object OnSubmit: UpdateProfileAction

    data class OnFirstNameChange(val value: String) : UpdateProfileAction

    data class OnLastNameChange(val value: String) : UpdateProfileAction

    data class OnPhoneNumberChange(val value: String) : UpdateProfileAction

    data class OnAgeChange(val value: String) : UpdateProfileAction

    data class OnGenderChange(val value: String) : UpdateProfileAction

    data class OnEducationLevelChange(val value: String) : UpdateProfileAction

    data class OnMaritalStatusChange(val value: String) : UpdateProfileAction

    data class OnConfirmPasswordChange(val value: String) : UpdateProfileAction

    data class OnHasTestedBeforeChange(val value: Boolean) : UpdateProfileAction

    data class OnImageChange(val value: ByteArray) : UpdateProfileAction

    data class OnShowImagePicker(val value: Boolean) : UpdateProfileAction

    data class OnCountryChange(val country: String) : UpdateProfileAction

}