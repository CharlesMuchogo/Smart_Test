package com.charlesmuchogo.research.presentation.language

import com.charlesmuchogo.research.domain.language.Language

sealed interface LanguageAction {
  data class OnUpdateLanguage(val language: Language): LanguageAction
}