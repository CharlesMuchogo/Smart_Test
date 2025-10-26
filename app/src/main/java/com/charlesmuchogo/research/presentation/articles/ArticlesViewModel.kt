package com.charlesmuchogo.research.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.charlesmuchogo.research.domain.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(pager: Pager<Int, Article>): ViewModel() {

    val articlesFlow =
        pager
            .flow
            .cachedIn(viewModelScope)

}