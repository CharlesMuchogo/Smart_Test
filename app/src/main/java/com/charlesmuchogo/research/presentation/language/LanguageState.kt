package com.charlesmuchogo.research.presentation.language

import com.charlesmuchogo.research.domain.language.Language

data class LanguageState(
    val languages : List<Language> = Language.languages,
    val selectedLanguage: Language = Language.languages.first(),
)