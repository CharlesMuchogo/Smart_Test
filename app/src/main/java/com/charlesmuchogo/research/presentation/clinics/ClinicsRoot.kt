package com.charlesmuchogo.research.presentation.clinics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.SearchClinicsPage
import com.charlesmuchogo.research.presentation.authentication.AuthControllerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicsRoot() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.find_clinic),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(SearchClinicsPage) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AuthControllerScreen(showTopBar = false) {
                ClinicsScreen()
            }
        }
    }
}
