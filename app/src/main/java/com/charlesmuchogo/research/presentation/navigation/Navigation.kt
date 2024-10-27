package com.charlesmuchogo.research.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.charlesmuchogo.research.presentation.authentication.LoginScreen
import com.charlesmuchogo.research.presentation.authentication.RegistrationScreen
import com.charlesmuchogo.research.presentation.bottomBar.HomeScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen
import com.charlesmuchogo.research.presentation.testpage.TestScreen


@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthController,
    ) {
        composable<LoginPage> {
            LoginScreen(navController = navController)
        }

        composable<RegistrationPage> {
            RegistrationScreen()
        }

        composable<HomePage>{
            HomeScreen()
         }

        composable<HistoryPage> {
            HistoryScreen()
        }

        composable<ProfilePage> {
            ProfileScreen()
        }

        composable<TestPage>{
            TestScreen()
        }

    }
}