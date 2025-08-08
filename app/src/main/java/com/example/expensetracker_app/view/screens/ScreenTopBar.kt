package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(title: String, subtitle: String? = null) {
    TopAppBar(
        title = {
            Column {
                Text(title)
                if (!subtitle.isNullOrEmpty()) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    )
}


@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String)->Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SpacerVertical(heightDp: Int = 8) {
    Spacer(modifier = Modifier.height(heightDp.dp))
}