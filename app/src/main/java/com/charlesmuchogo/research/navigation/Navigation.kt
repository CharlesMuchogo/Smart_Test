package com.charlesmuchogo.research.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.charlesmuchogo.research.data.network.httpUrlBuilder
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.articles.ArticleDetailsScreen
import com.charlesmuchogo.research.presentation.authentication.OnBoardingControllerScreen
import com.charlesmuchogo.research.presentation.authentication.MoreDetailsScreen
import com.charlesmuchogo.research.presentation.authentication.RegistrationScreen
import com.charlesmuchogo.research.presentation.authentication.forgotPassword.ForgotPasswordRoot
import com.charlesmuchogo.research.presentation.authentication.login.LoginRoot
import com.charlesmuchogo.research.presentation.authentication.setPasswordScreen.SetPasswordRoot
import com.charlesmuchogo.research.presentation.bottomBar.BottomBarRoot
import com.charlesmuchogo.research.presentation.chat.ChatRoot
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.clinics.SearchClinicsScreen
import com.charlesmuchogo.research.presentation.common.SnackBarContent
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.onboarding.OnboardingRoot
import com.charlesmuchogo.research.presentation.profile.EditProfileScreen
import com.charlesmuchogo.research.presentation.profile.PictureScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen
import com.charlesmuchogo.research.presentation.results.ResultsRoot
import com.charlesmuchogo.research.presentation.testpage.PendingTestScreen
import com.charlesmuchogo.research.presentation.testpage.TestAuthBlocker
import com.charlesmuchogo.research.presentation.testpage.TestInfoScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(navController: NavHostController) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackBarViewModel.events.collect { snackBarItem ->
            snackBarHostState.showSnackbar(
                message = snackBarItem.message,
                actionLabel = if(snackBarItem.isError) "error" else null,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackBarContent(
                modifier = Modifier.statusBarsPadding(),
                snackBarHostState = snackBarHostState,
            )
        }
    ) {
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = AuthController,
            ) {

                composable<AuthController> {
                    OnBoardingControllerScreen(
                        navController = navController,
                        animatedVisibilityScope = this
                    )
                }

                composable<OnBoardingScreen> {
                    OnboardingRoot()
                }
                composable<LoginPage> {
                    LoginRoot()
                }

                composable<ChatPage> {
                    ChatRoot()
                }

                composable<ForgotPasswordPage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) {
                    ForgotPasswordRoot()
                }

                composable<RegistrationPage> {
                    RegistrationScreen(navController = navController)
                }
                composable<MoreDetailsPage> {
                    MoreDetailsScreen()
                }

                composable<SetPasswordPage>(
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "${httpUrlBuilder()}/api/reset_password?id={token}"
                        },
                    ),
                ) { backStackEntry ->
                    val args = backStackEntry.toRoute<SetPasswordPage>()
                    SetPasswordRoot(token = args.token)
                }

                composable<HomePage> {
                    BottomBarRoot(animatedVisibilityScope = this)
                }

                composable<HistoryPage> {
                    HistoryScreen()
                }

                composable<ProfilePage> {
                    ProfileScreen()
                }

                composable<ArticleDetailsPage> { backStackEntry ->
                    val args = backStackEntry.toRoute<ArticleDetailsPage>()
                    ArticleDetailsScreen(id = args.id, animatedVisibilityScope = this)
                }

                composable<PhotoPage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) { backStackEntry ->
                    val args = backStackEntry.toRoute<PhotoPage>()
                    PictureScreen(
                        url = args.image,
                        title = args.title
                    )
                }

                composable<EditProfilePage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) {
                    EditProfileScreen()
                }
                composable<ClinicsPage> {
                    ClinicsScreen()
                }
                composable<SearchClinicsPage> {
                    SearchClinicsScreen(navController = navController)
                }

                composable<TestPage> {
                    TestAuthBlocker()
                }

                composable<PendingTestPage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) {
                    PendingTestScreen(navController = navController)
                }
                composable<TestInfoPage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) {
                    TestInfoScreen(navController = navController)
                }


                composable<TestResultsPage>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_DURATION),
                        )
                    },
                ) { backStackEntry ->
                    val args = backStackEntry.toRoute<TestResultsPage>()
                    ResultsRoot(id = args.id)
                }
            }
        }
    }
}

const val ANIMATION_DURATION = 300