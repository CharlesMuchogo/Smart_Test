package com.charlesmuchogo.research.presentation.onboarding

sealed interface OnboardingAction {
    data class SaveFirstTime(val isFirstTime: Boolean): OnboardingAction

    data class OnChangeSlide(val slide: Int): OnboardingAction

}