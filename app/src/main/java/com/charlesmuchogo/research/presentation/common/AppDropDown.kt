
package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.domain.models.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropDown(
    modifier: Modifier = Modifier,
    label: @Composable() (() -> Unit)? = null,
    options: List<T>,
    selectedOption: TextFieldState,
    onOptionSelected: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        label?.invoke()
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Box(
                modifier = modifier.border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Gray
                ),
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    value = selectedOption.text ,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = if (selectedOption.isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                )
            }
            ExposedDropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { content(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        colors = MenuItemColors(
                            textColor = MaterialTheme.colorScheme.onBackground,
                            trailingIconColor = MaterialTheme.colorScheme.onBackground,
                            disabledTextColor = Color.Transparent,
                            disabledLeadingIconColor = Color.Transparent,
                            disabledTrailingIconColor = Color.Transparent,
                            leadingIconColor = Color.Transparent,
                        ),
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (!selectedOption.error.isNullOrEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = selectedOption.error,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Start
            )
        }
    }
}
