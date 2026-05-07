package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun InfoLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun InfoLabelPreview() {
    NeupsiproMovilTheme {
        InfoLabel(text = "Fecha de ingreso neuro:")
    }
}