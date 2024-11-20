package com.charlesmuchogo.research

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.charlesmuchogo.research.domain.viewmodels.AuthenticationViewModel
import com.charlesmuchogo.research.presentation.navigation.Navigation
import com.charlesmuchogo.research.presentation.utils.RequestPermissions
import dagger.hilt.android.AndroidEntryPoint
import ui.theme.SmartTestTheme
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            val profileStatus =   authenticationViewModel.profileStatus.collectAsStateWithLifecycle().value
            RequestPermissions()

            SmartTestTheme(dynamicColor = false, darkTheme = profileStatus.data?.darkTheme ?: false) {
                Surface(color = MaterialTheme.colorScheme.background){
                    Navigation()
                }
            }
        }
    }


}
