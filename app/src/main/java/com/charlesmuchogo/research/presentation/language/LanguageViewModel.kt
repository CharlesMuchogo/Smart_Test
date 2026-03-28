package com.charlesmuchogo.research.presentation.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.language.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.Hash.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val database: AppDatabase,
    private val settingsRepository: MultiplatformSettingsRepository,
) : ViewModel() {

    init {
        insertLanguages()
    }

    private fun insertLanguages() {

    }

    private val _state = MutableStateFlow(LanguageState())
    val state = combine(_state, settingsRepository.getSelectedLanguage()){ state, language ->
        state.copy(selectedLanguage = Language.languages.firstOrNull{it.code == language} ?: state.selectedLanguage)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LanguageState()
        )

    fun onAction(action: LanguageAction) {
        when (action) {
            is LanguageAction.OnUpdateLanguage -> {

                settingsRepository.saveSelectedLanguage(action.language.code)

                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(action.language.code)
                )

                _state.update { it.copy(selectedLanguage = action.language) }
            }
        }
    }

}