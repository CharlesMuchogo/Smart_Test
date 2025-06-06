package com.charlesmuchogo.research.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLoadingDialog(
    onDismiss: ()-> Unit = {},
    label: String = "Loading..."
){
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Box(modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium).background(
                MaterialTheme.colorScheme.background)){
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)){
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text( label)
                }
            }
        }
    )
}