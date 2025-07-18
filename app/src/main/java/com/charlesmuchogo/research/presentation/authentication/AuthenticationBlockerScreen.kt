package com.charlesmuchogo.research.presentation.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.LoginPage
import com.charlesmuchogo.research.navigation.RegistrationPage
import com.charlesmuchogo.research.presentation.utils.removeRipple

@Composable
fun AuthenticationBlockerScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.password),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxHeight(0.4f),
        )

        Text(
            text = "Welcome to Smarttest, we are glad to have you here! Login to proceed",
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
        ) {
            Text(
                text = "Already have an account? ",
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    ),
            )

            Text(
                modifier = Modifier.removeRipple { navController.navigate(LoginPage) },
                text = "Login",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    ),
            )

            Text(
                text = "Or",
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    ),
            )

            Text(
                modifier = Modifier.removeRipple { navController.navigate(RegistrationPage) },
                text = "Sign Up",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    ),
            )
        }
    }
}
