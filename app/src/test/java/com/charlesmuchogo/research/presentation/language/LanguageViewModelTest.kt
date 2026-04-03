package com.charlesmuchogo.research.presentation.language

import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.domain.language.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LanguageViewModelTest {

    private lateinit var viewModel: LanguageViewModel
    private val database: AppDatabase = mock()
    private val settingsRepository: MultiplatformSettingsRepository = mock()
    private val testDispatcher = StandardTestDispatcher()
    private val languageFlow = MutableStateFlow("en")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(settingsRepository.getSelectedLanguage()).thenReturn(languageFlow)
        viewModel = LanguageViewModel(database, settingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have language from repository`() = runTest {
        whenever(settingsRepository.getSelectedLanguage()).thenReturn(flowOf("es"))
        // Create new viewModel to trigger init/state gathering with the new flow
        val esViewModel = LanguageViewModel(database, settingsRepository)
        
        // Use backgroundScope to collect the state flow
        val states = mutableListOf<LanguageState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            esViewModel.state.collect { states.add(it) }
        }
        
        advanceUntilIdle()
        
        assertEquals("es", states.last().selectedLanguage.code)
        job.cancel()
    }

    @Test
    fun `onAction(OnUpdateLanguage) should update repository and state`() = runTest {
        val selectedLanguage = Language.languages.first { it.code == "fr" }
        
        // Use backgroundScope to collect the state flow
        val states = mutableListOf<LanguageState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { states.add(it) }
        }
        
        advanceUntilIdle() // Ensure initial state is collected
        
        viewModel.onAction(LanguageAction.OnUpdateLanguage(selectedLanguage))
        
        // Check if repository is updated
        verify(settingsRepository).saveSelectedLanguage("fr")
        
        // Simulate the repository emitting the new language after it was saved
        languageFlow.value = "fr"
        
        advanceUntilIdle()
        
        assertEquals("fr", states.last().selectedLanguage.code)
        job.cancel()
    }
}
