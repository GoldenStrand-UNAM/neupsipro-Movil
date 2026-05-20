package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StatusTag(
    status: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
) {
    Text(
        text = status.lowercase(),
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier
    )
}
