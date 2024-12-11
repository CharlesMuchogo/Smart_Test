package com.charlesmuchogo.research.domain.states

import com.charlesmuchogo.research.presentation.utils.genders
import com.charlesmuchogo.research.presentation.utils.levelsOfEducation

data class LoginState(
    val firstName: String = "",
    val lastname: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val rememberMe: Boolean = true,
    val termsAndConditions: Boolean = false,

    val age: String = "",
    val gender: String = "",
    val educationLevel: String = "",
    val hasTestedBefore: Boolean = false,
    val maritalStatus: String = "",

    //Errors
    val domainError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val firstNameError: String? = null,
    val lastnameError: String? = null,
    val phoneNumberError: String? = null,
    val ageError: String? = null,
    val genderError: String? = null,
    val educationLevelError: String? = null,
    val maritalStatusError: String? = null,

    )
