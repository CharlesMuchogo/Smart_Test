package com.charlesmuchogo.research.domain.models

data class TextFieldState (
    val text: String = "",
    val isSelected: Boolean = false,
    val error: String? = null
)
