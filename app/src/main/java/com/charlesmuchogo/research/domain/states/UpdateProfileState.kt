package com.charlesmuchogo.research.domain.states

data class UpdateProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val educationLevel: String = "",
    val maritalStatus: String = "",
    val testedBefore: Boolean = false,
    val showImagePicker: Boolean = false,
    val image: ByteArray? = null,
    val imageLink: String? = null,
    val isSubmitting: Boolean = false,
    val hasSubmitted: Boolean = false,


    /** Errors */
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneNumberError: String? = null,
    val dateOfBirthError: String? = null,
    val maritalStatusError: String? = null,
    val genderError: String? = null,
    val educationLevelError: String? = null,
    val testedBeforeError: String? = null,
)
