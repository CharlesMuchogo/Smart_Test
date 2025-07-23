package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.MoreDetailsPage
import com.charlesmuchogo.research.presentation.common.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreDetailsRequiredScreen(showTopBar: Boolean = false) {
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showTopBar
            ) {
                TopAppBar(
                    title = {  },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        }
    ){
        Column(modifier = Modifier.padding(it).fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
            Image(
                painter = painterResource(R.drawable.completeregistration),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier.fillMaxHeight(0.15f).padding(vertical = 8.dp)
            )

            Text(stringResource(R.string.almost_there), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), textAlign = TextAlign.Center)

            Text(stringResource(R.string.we_need_more_information), textAlign = TextAlign.Start)

            Spacer(modifier = Modifier.weight(1f))
            AppButton(onClick ={ navController.navigate(MoreDetailsPage)} ) {
                 Text("Complete Registration")
             }
        }
    }
}