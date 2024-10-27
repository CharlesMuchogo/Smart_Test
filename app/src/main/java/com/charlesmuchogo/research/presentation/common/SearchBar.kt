package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    description: String = "search",
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
) {
    Box {
        TextField(
            value = value,
            placeholder = {
                Text(
                    text = description,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            onValueChange = onValueChange,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
            shape = RoundedCornerShape(30.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors =
                TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            leadingIcon = {
                IconButton(
                    onClick = onExit,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = "",
                    )
                }
            },
            modifier =
                modifier
                    .testTag(description),
        )
    }
}
