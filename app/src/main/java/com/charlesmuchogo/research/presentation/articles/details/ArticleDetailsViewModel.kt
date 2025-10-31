package com.charlesmuchogo.research.presentation.articles.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.domain.mappers.toFavoriteArticle
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(private val database: AppDatabase): ViewModel() {
    val articleState = MutableStateFlow(Results.initial<Article>())

    fun getArticle(id:String, showLoading: Boolean = true){
        viewModelScope.launch {

            if(showLoading){
                articleState.value = Results.loading()
            }

            database.articleDao().getArticle(id).catch {
                articleState.value = Results.error()
            }.collect{
                val isFavorite = database.favoriteArticleDao().getArticle(id).firstOrNull()
                articleState.value = Results.success(it?.copy(isFavorite = isFavorite != null))
            }
        }
    }

    fun favoriteArticle(article: Article){
        viewModelScope.launch {
            database.favoriteArticleDao().insertArticle(article.toFavoriteArticle())
            getArticle(id = article.id, showLoading = false)
        }
    }

    fun unFavoriteArticle(article: Article){
        viewModelScope.launch {
            database.favoriteArticleDao().deleteArticle(article.toFavoriteArticle())
            getArticle(id = article.id, showLoading = false)
        }
    }
}