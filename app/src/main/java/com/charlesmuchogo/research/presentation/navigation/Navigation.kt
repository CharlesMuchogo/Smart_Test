package com.charlesmuchogo.research.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen
import com.charlesmuchogo.research.presentation.authentication.ForgotPasswordScreen
import com.charlesmuchogo.research.presentation.authentication.LoginScreen
import com.charlesmuchogo.research.presentation.authentication.MoreDetailsScreen
import com.charlesmuchogo.research.presentation.authentication.RegistrationScreen
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.clinics.SearchClinicsScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen
import com.charlesmuchogo.research.presentation.testpage.PendingTestScreen
import com.charlesmuchogo.research.presentation.testpage.TestInfoScreen
import com.charlesmuchogo.research.presentation.testpage.TestScreen


@Composable
fun Navigation(navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = AuthController,
    ) {

        composable<AuthController> {
            AuthControllerScreen(navController = navController)
        }
        composable<LoginPage> {
            LoginScreen(navController = navController)
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

    }
}

const val ANIMATION_DURATION = 400