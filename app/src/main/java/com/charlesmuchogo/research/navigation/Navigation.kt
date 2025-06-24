package com.charlesmuchogo.research.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.navigation.toRoute
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.authentication.ForgotPasswordScreen
import com.charlesmuchogo.research.presentation.authentication.login.LoginRoot
import com.charlesmuchogo.research.presentation.authentication.MoreDetailsScreen
import com.charlesmuchogo.research.presentation.authentication.RegistrationScreen
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
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
import com.charlesmuchogo.research.presentation.testpage.TestInfoScreen
import com.charlesmuchogo.research.presentation.testpage.TestScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(navController: NavHostController) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackBarViewModel.events.collect { snackBarItem ->
            snackBarHostState.showSnackbar(
                message = snackBarItem.message,
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
        NavHost(
            navController = navController,
            startDestination = AuthController,
        ) {

            composable<AuthController> {
                AuthControllerScreen(navController = navController)
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

            composable<ForgotPasswordPage> {
                ForgotPasswordScreen(navController = navController)
            }

            composable<RegistrationPage> {
                RegistrationScreen(navController = navController)
            }
            composable<MoreDetailsPage> {
                MoreDetailsScreen(navController = navController)
            }

            composable<HomePage> {
                HomeScreen(navController = navController)
            }

            composable<HistoryPage> {
                HistoryScreen(navController = navController)
            }

            composable<ProfilePage> {
                ProfileScreen(navController = navController)
            }
            composable<PhotoPage> (
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
            ){  backStackEntry ->
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
                TestScreen(navController = navController)
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
                ResultsRoot(id = args.id )
            }
        }
    }

}

const val ANIMATION_DURATION = 300