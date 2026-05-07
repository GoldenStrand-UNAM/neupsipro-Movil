package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun StatusTag(
    status: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = status.lowercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun StatusTagPreview() {
    NeupsiproMovilTheme {
        StatusTag(status = "Activo")
    }
}