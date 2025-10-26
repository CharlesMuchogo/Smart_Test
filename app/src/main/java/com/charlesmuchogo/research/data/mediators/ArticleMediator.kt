package com.charlesmuchogo.research.data.mediators

import android.net.http.HttpException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.models.Article
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ArticleMediator(
    private val remoteRepository: RemoteRepository,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Article>() {

    private val articleDao = appDatabase.articleDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val page = (articleDao.count() / state.config.pageSize) + 1
                    page
                }
            }

            val response = remoteRepository.fetchArticles(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            val articles = response.data?.posts ?: emptyList()

            if (loadType == LoadType.REFRESH) {
                articleDao.deleteArticles()
            }

            articleDao.insertArticles(articles)

            MediatorResult.Success(
                endOfPaginationReached = articles.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
