package com.charlesmuchogo.research.presentation.testpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.charlesmuchogo.research.R





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInfoScreen(navController: NavController) {

    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {

            IconButton(onClick = {
                navController.popBackStack()
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding).padding(horizontal = 8.dp)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    Spacer(modifier = Modifier.fillMaxSize(0.3f))
                }
                item {
                    Image(
                        painter = painterResource(id = R.drawable.reminder),
                        modifier = Modifier
                            .fillParentMaxWidth(0.4f)
                            .fillParentMaxHeight(0.4f),
                        contentDescription = "picture"
                    )
                }

                item {
                    Text( modifier = Modifier.padding(bottom = 12.dp), text = "Your test was submitted successfully. Please wait for the confirmation of the results.", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary))
                }

                item {
                        Text(
                            text = "If your test result is positive, please follow up with a clinic checkup for further guidance and support.",
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                        )

                }

                item {
                        Text(
                            text = "If your test result is negative and you've engaged in activities that may lead to HIV exposure recently, consider testing again after 3 months.",
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                        )

                }
            }
        }
    }
}
