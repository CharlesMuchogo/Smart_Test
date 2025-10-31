package com.charlesmuchogo.research.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.presentation.instructions.InstructionsAction
import com.charlesmuchogo.research.presentation.instructions.InstructionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(pager: Pager<Int, Article>): ViewModel() {

    val articlesFlow =
        pager
            .flow
            .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(ArticlesState())


    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ArticlesState()
    )


    fun onAction(action: ArticlesAction){
        when(action){
            ArticlesAction.OnHasShownAd -> {
                _state.update { it.copy(showAd = !it.showAd) }
            }
        }
    }

}