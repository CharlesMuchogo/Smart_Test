package com.charlesmuchogo.research

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.navigation.Navigation
import com.charlesmuchogo.research.presentation.utils.RequestPermissions
import com.charlesmuchogo.research.presentation.utils.setAppLocale
import com.charlesmuchogo.research.ui.theme.SmartTestTheme
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController

lateinit var analytics: FirebaseAnalytics

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = PreferenceManager.SERVER_ID))

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

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForUpdate()

        val availability = GoogleApiAvailability.getInstance()
        val status = availability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            Log.e("PlayServices", "Google Play Services is not available!")
        }

        setAppLocale(context = this, languageCode = "en")

        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {}
        }

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            navController = rememberNavController()

            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()

            val darkThemeFlow by authenticationViewModel.appTheme.collectAsStateWithLifecycle()

            val darkTheme = when (darkThemeFlow) {
                0 -> false
                1 -> true
                else -> isSystemInDarkTheme()
            }

            RequestPermissions()
            SmartTestTheme(
                dynamicColor = false,
                darkTheme = darkTheme
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val density = LocalDensity.current.density
                    CompositionLocalProvider(LocalDensity provides Density(density = density * 0.93f)) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    AppUpdateType.IMMEDIATE ,
                    this,
                    123,
                )
            }
        }
    }

    private fun checkForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = info.isFlexibleUpdateAllowed

            if (isUpdateAllowed && isUpdateAvailable) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    AppUpdateType.IMMEDIATE ,
                    this,
                    123,
                )
            }
        }
    }

}
