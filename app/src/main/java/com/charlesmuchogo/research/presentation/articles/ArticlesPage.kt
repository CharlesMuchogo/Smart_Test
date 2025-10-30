package com.charlesmuchogo.research.presentation.articles

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ArticleDetailsPage
import com.charlesmuchogo.research.presentation.common.AppEmptyScreen
import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.common.HtmlText
import com.charlesmuchogo.research.presentation.profile.ProfileIcon
import com.charlesmuchogo.research.presentation.utils.timeAgo


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesScreen(animatedVisibilityScope: AnimatedVisibilityScope) {

    val articlesViewModel = hiltViewModel<ArticlesViewModel>()
    val articlesFlow = articlesViewModel.articlesFlow.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        if (articlesFlow.loadState.refresh is LoadState.Error && articlesFlow.itemCount < 1) {
            AppErrorScreen(
                errorDescription = "${(articlesFlow.loadState.refresh as LoadState.Error).error.message}",
                onRetry = { articlesFlow.retry() }
            )
        } else if (articlesFlow.loadState.refresh is LoadState.Loading && articlesFlow.itemCount < 1) {
            AppListLoading(modifier = Modifier.padding(horizontal = 8.dp))
        } else {
            if (articlesFlow.itemCount < 1) {
                AppEmptyScreen(pageDescription = "No articles at the moment. come back later")
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(count = articlesFlow.itemCount, key = { it }) { index ->
                        articlesFlow[index]?.let{ article ->
                            ArticleCard(
                                article = article,
                                animatedVisibilityScope = animatedVisibilityScope
                            )

                            HorizontalDivider(
                                color = Color.Gray,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 24.dp)
                            )
                        }
                    }

                    item {
                        if (articlesFlow.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp),
                                strokeWidth = 3.dp,
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.navigationBarsPadding()) }

                }
            }
        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesListVIew(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(articles, key = { it.id }) { article ->
            ArticleCard(article = article, animatedVisibilityScope = animatedVisibilityScope)

            HorizontalDivider(
                color = Color.Gray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 24.dp)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    ListItem(
        modifier = modifier.clickable {
           navController.navigate(ArticleDetailsPage(id = article.id))
        },
        leadingContent = {
            ProfileIcon(
                image = article.image,
                modifier = Modifier
                    .size(60.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = article.image),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 500)

                        }
                    ),
                iconSize = 40.dp,
                icon = Icons.Default.Image,
            )
        },
        headlineContent = {
            Text(
                article.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.sharedElement(
                    sharedContentState = rememberSharedContentState(key = article.title),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 500)

                    }
                )
            )
        },
        supportingContent = {
            Column(modifier = Modifier.fillMaxWidth()){
                HtmlText(
                    htmlContent = article.content,
                    maxLines = 2,
                )

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically){
                    Text(timeAgo(article.createdAt), modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Back",
                        modifier = Modifier.padding(horizontal = 8.dp).size(20.dp)
                    )
                }
            }
        }
    )
}