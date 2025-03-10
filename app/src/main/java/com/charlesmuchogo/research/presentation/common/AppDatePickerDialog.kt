package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppDatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        ),
) {
    DatePickerDialog(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 8.dp),
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedTimestamp =
                        datePickerState.selectedDateMillis ?: Clock.System.now()
                            .toEpochMilliseconds()
                    onDateSelected.invoke(selectedTimestamp)
                    onDismiss.invoke()
                },
            ) {
                Text(text = "OK")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
