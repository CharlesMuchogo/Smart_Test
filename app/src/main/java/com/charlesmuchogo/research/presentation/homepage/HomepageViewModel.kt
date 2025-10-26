package com.charlesmuchogo.research.presentation.homepage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor() : ViewModel() {

    val selectedTab = MutableStateFlow(0)

    fun updateTab(tab : Int){
        selectedTab.value = tab
    }
}

enum class HomepageTabs{
    INSTRUCTIONS, ARTICLES
}