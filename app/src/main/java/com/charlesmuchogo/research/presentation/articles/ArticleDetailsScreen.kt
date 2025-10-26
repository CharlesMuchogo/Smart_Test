package com.charlesmuchogo.research.presentation.articles

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.HtmlText
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.PhotoPage
import com.charlesmuchogo.research.presentation.common.CenteredColumn
import com.charlesmuchogo.research.presentation.common.HtmlText
import com.charlesmuchogo.research.presentation.utils.ResultStatus

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticleDetailsScreen(
    id: String,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val articleDetailsViewModel = hiltViewModel<ArticleDetailsViewModel>()
    val articleState by articleDetailsViewModel.articleState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()



    LaunchedEffect(id) {
        articleDetailsViewModel.getArticle(id)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = Color.Transparent,
                    containerColor = Color.Transparent,
                ),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {

                },
                actions = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = {
                            articleState.data?.let {
                                if (it.isFavorite) {
                                    articleDetailsViewModel.unFavoriteArticle(it)
                                } else {
                                    articleDetailsViewModel.favoriteArticle(it)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = when (articleState.data?.isFavorite) {
                                true -> {
                                    Icons.Filled.Favorite
                                }

                                else -> {
                                    Icons.Default.FavoriteBorder
                                }
                            },

                            tint = when (articleState.data?.isFavorite) {
                                true -> {
                                    MaterialTheme.colorScheme.primary
                                }

                                else -> {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            },
                            contentDescription = "Back"
                        )

                    }
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {


            when (articleState.status) {
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    CenteredColumn {
                        CircularProgressIndicator()
                    }
                }

                ResultStatus.ERROR -> {
                    CenteredColumn {
                        Text("Something went wrong. Try again")
                    }
                }

                ResultStatus.SUCCESS -> {
                    if (articleState.data == null) {
                        CenteredColumn {
                            Text("This article is not available at the moment. Come back later")
                        }
                    } else {
                        ArticlesDetails(
                            modifier = Modifier,
                            article = articleState.data!!,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesDetails(
    modifier: Modifier = Modifier,
    article: Article,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            SubcomposeAsyncImage(
                modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            navController.navigate(
                                PhotoPage(
                                    title = "Photo",
                                    image = article.image
                                )
                            )
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = article.image),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 500)

                        }
                    ),
                model = article.image,
                contentScale = ContentScale.Crop,
                contentDescription = article.image,
                loading = {
                    CenteredColumn {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier =
                            Modifier
                                .size(25.dp),
                        )
                    }
                },
                error = {
                    it.result
                    CenteredColumn {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            imageVector = Icons.Default.Image,
                            contentDescription = "image",
                            modifier = Modifier.size(56.dp),
                        )
                    }
                },
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = article.title),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 500)

                    }
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // HTML Content
        HtmlText(
            htmlContent = article.content,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
