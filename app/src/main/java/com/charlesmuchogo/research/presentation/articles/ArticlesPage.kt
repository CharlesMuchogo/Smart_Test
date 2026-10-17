package com.charlesmuchogo.research.presentation.articles

import android.text.Html
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.ads.BannerAd
import com.charlesmuchogo.research.ads.BannerStyle
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.ArticleDetailsPage
import com.charlesmuchogo.research.presentation.common.AppEmptyScreen
import com.charlesmuchogo.research.presentation.common.AppErrorScreen
import com.charlesmuchogo.research.presentation.common.AppListLoading
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard
import com.charlesmuchogo.research.presentation.utils.timeAgo

private const val WORDS_PER_MINUTE = 200
private const val FIRST_INLINE_AD_INDEX = 3
private const val INLINE_AD_INTERVAL = 8

private fun showsInlineAd(index: Int): Boolean =
    index >= FIRST_INLINE_AD_INDEX && (index - FIRST_INLINE_AD_INDEX) % INLINE_AD_INTERVAL == 0

private fun plainText(html: String): String = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()

private fun readingMinutes(html: String): Int =
    (plainText(html).split(Regex("\\s+")).count { it.isNotBlank() } / WORDS_PER_MINUTE).coerceAtLeast(1)

private fun safeTimeAgo(value: String): String = runCatching { timeAgo(value) }.getOrDefault("")

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesScreen(animatedVisibilityScope: AnimatedVisibilityScope) {

    val articlesViewModel = hiltViewModel<ArticlesViewModel>()
    val articlesFlow = articlesViewModel.articlesFlow.collectAsLazyPagingItems()
    var query by rememberSaveable { mutableStateOf("") }

    val onOpen: (Article) -> Unit = { article ->
        navController.navigate(ArticleDetailsPage(id = article.id))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (articlesFlow.loadState.refresh is LoadState.Error && articlesFlow.itemCount < 1) {
            AppErrorScreen(
                errorDescription = (articlesFlow.loadState.refresh as LoadState.Error).error.message,
                onRetry = { articlesFlow.retry() }
            )
        } else if (articlesFlow.loadState.refresh is LoadState.Loading && articlesFlow.itemCount < 1) {
            AppListLoading(modifier = Modifier.padding(horizontal = 8.dp))
        } else if (articlesFlow.itemCount < 1) {
            AppEmptyScreen(pageDescription = stringResource(R.string.no_articles))
        } else {
            val searching = query.isNotBlank()
            val matches = if (searching) {
                articlesFlow.itemSnapshotList.items.filter {
                    it.title.contains(query.trim(), ignoreCase = true)
                }
            } else {
                emptyList()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    ArticleSearchField(
                        query = query,
                        onQueryChange = { query = it },
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }

                if (searching) {
                    if (matches.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_matching_articles),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 24.dp),
                            )
                        }
                    }
                    items(matches, key = { it.id }) { article ->
                        ArticleRow(article = article, animatedVisibilityScope = animatedVisibilityScope, onOpen = { onOpen(article) })
                    }
                } else {
                    items(count = articlesFlow.itemCount, key = { index -> articlesFlow.peek(index)?.id ?: index }) { index ->
                        Column {
                            articlesFlow[index]?.let { article ->
                                if (index == 0) {
                                    FeaturedArticleCard(article = article, animatedVisibilityScope = animatedVisibilityScope, onOpen = { onOpen(article) })
                                } else {
                                    ArticleRow(article = article, animatedVisibilityScope = animatedVisibilityScope, onOpen = { onOpen(article) })
                                }
                            }
                            if (showsInlineAd(index)) {
                                BannerAd(style = BannerStyle.INLINE, modifier = Modifier.padding(top = 10.dp))
                            }
                        }
                    }

                    item {
                        if (articlesFlow.loadState.append is LoadState.Loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp),
                                    strokeWidth = 3.dp,
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun ArticleSearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(start = 14.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_articles),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.clear_search))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ArticleImage(
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = article.image),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ -> tween(durationMillis = 500) },
            )
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.MenuBook,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        if (article.image.isNotBlank()) {
            AsyncImage(
                model = article.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ArticleTitle(
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope,
    featured: Boolean,
) {
    Text(
        text = article.title,
        style = if (featured) {
            MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        } else {
            MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        },
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.sharedElement(
            sharedContentState = rememberSharedContentState(key = article.title),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ -> tween(durationMillis = 500) },
        ),
    )
}

@Composable
private fun articleMeta(article: Article): String {
    val minutes = remember(article.content) { readingMinutes(article.content) }
    val updated = remember(article.createdAt) { safeTimeAgo(article.createdAt) }
    val read = pluralStringResource(R.plurals.min_read, minutes, minutes)
    return listOf(read, updated).filter { it.isNotBlank() }.joinToString(" · ")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.FeaturedArticleCard(
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onOpen: () -> Unit,
) {
    val excerpt = remember(article.content) { plainText(article.content).trim() }
    SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = onOpen) {
        Column {
            ArticleImage(
                article = article,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.featured),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                ArticleTitle(article = article, animatedVisibilityScope = animatedVisibilityScope, featured = true)
                if (excerpt.isNotBlank()) {
                    Text(
                        text = excerpt,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = articleMeta(article),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ArticleRow(
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onOpen: () -> Unit,
) {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), onClick = onOpen) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ArticleImage(
                article = article,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.small),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ArticleTitle(article = article, animatedVisibilityScope = animatedVisibilityScope, featured = false)
                Text(
                    text = articleMeta(article),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
