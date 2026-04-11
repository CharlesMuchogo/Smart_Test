package com.charlesmuchogo.research.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.WebsocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val websocketRepository: WebsocketRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            websocketRepository.connectWebSocket()
        }
    }

    val selectedTab = MutableStateFlow(0)

    fun updateTab(tab : Int){
        selectedTab.value = tab
    }
}

enum class HomepageTabs{
    INSTRUCTIONS, ARTICLES
}