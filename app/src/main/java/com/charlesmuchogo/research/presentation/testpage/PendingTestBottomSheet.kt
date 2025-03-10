package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTestScreen(navController: NavController) {

    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    12.dp,
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {


                Image(
                    painter = painterResource(id = R.drawable.wait),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight(0.4f),
                    contentDescription = "picture"
                )



                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.testUnderReview),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Medium
                    )
                )


                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.waitForReview),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
                )

                Spacer(modifier = Modifier.weight(1f))

                AppButton( shape = MaterialTheme.shapes.extraLarge,onClick = { navController.popBackStack() }) {
                    Text(text = stringResource(R.string.okay))
                }
            }
        }
    }
}
