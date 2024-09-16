package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    placeholder: String,
    error: String?,
    maxCharacters: Int = Int.MAX_VALUE,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType,
    passwordVisible: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    trailingIcon: @Composable () -> Unit = {},
) {
    Column(modifier) {
        var currentError by remember { mutableStateOf(error) }
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 5.dp),
            )
        }

        OutlinedTextField(
            value = value,
            placeholder = { Text(text = placeholder, color = Color.Gray) },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                ),
            onValueChange = {
                if (it.length <= maxCharacters) {
                    onValueChanged(it)
                    currentError = null
                } else {
                    currentError = "$label cannot be more than $maxCharacters characters"
                }
            },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            trailingIcon = trailingIcon,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = unfocusedBorderColor,
                ),
        )

        if (maxCharacters != Int.MAX_VALUE) {
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.End),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currentError ?: " ",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelLarge,
                )

                Text(
                    "${value.length}/$maxCharacters",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
