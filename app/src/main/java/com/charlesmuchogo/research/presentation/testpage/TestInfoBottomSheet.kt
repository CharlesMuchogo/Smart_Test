package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.common.AppButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInfoScreen(navController: NavController) {

    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

        })
    }) { padding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.wait),
                modifier = Modifier
                    .fillMaxHeight(0.4f),
                contentScale = ContentScale.Fit,
                contentDescription = "picture"
            )



            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 24.dp),
                text = stringResource(R.string.weAreReviewingYourTest) ,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
            )


            Text(
                text = stringResource(R.string.positiveTestResult),
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )

            Text(
                text = stringResource(R.string.negativeTestResult),
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )


            Spacer(Modifier.weight(1f))

            AppButton(
                shape = MaterialTheme.shapes.extraLarge,
                onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.okay))
            }
        }


    }
}
