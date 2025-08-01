package com.charlesmuchogo.research.navigation

import kotlinx.serialization.Serializable


@Serializable
object AuthController

@Serializable
object OnBoardingScreen

@Serializable
object LoginPage

@Serializable
object ForgotPasswordPage

@Serializable
object RegistrationPage

@Serializable
object MoreDetailsPage

@Serializable
data class SetPasswordPage(
    val token: String
)

@Serializable
object HomePage

@Serializable
object TestPage

@Serializable
data class TestResultsPage(
    val id: Long
)

@Serializable
object ProfilePage

@Serializable
object EditProfilePage

@Serializable
object HistoryPage

@Serializable
object ClinicsPage

@Serializable
object SearchClinicsPage

@Serializable
object PendingTestPage

@Serializable
object TestInfoPage

@Serializable
object ChatPage

@Serializable
data class PhotoPage(val image: String, val title: String = "Image")



