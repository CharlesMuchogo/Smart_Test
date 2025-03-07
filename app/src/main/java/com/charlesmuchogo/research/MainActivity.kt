package com.charlesmuchogo.research

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.navigation.Navigation
import com.charlesmuchogo.research.presentation.utils.RequestPermissions
import com.charlesmuchogo.research.ui.theme.SmartTestTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle =
            SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
            navigationBarStyle =
            SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
        )

        val availability = GoogleApiAvailability.getInstance()
        val status = availability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            Log.e("PlayServices", "Google Play Services is not available!")
        }


        setContent {
            navController = rememberNavController()

            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            val profileStatus =
                authenticationViewModel.profileStatus.collectAsStateWithLifecycle().value
            RequestPermissions()
            SmartTestTheme(
                dynamicColor = false,
                darkTheme = profileStatus.data?.darkTheme ?: false
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val density = LocalDensity.current.density

                    CompositionLocalProvider(LocalDensity provides Density(density = density * 0.93f)) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }


}
