package com.charlesmuchogo.research.presentation.language

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charlesmuchogo.research.presentation.common.NavigationIcon
import com.charlesmuchogo.research.presentation.common.TopBarTitle
import com.charlesmuchogo.research.presentation.utils.removeRipple


@Composable
fun LanguageRoot() {

    val viewModel = hiltViewModel<LanguageViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LanguageScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    state: LanguageState,
    onAction: (LanguageAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { NavigationIcon() },
                title = {
                    TopBarTitle("Select Language")
                },
            )
        }
    ){ innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            items(state.languages){
                ListItem(
                    modifier = Modifier.removeRipple{onAction(LanguageAction.OnUpdateLanguage(it))},
                    headlineContent = { Text(it.name, color = if(it == state.selectedLanguage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground) },
                    supportingContent = {Text(it.country, color = if(it == state.selectedLanguage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)},
                    trailingContent = { Text(it.code, color = if(it == state.selectedLanguage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground) },
                )
            }
        }
    }

}