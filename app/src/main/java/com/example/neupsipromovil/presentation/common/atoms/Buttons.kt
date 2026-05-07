package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun AccessibilityButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = CircleShape,
        icon = { Icon(Icons.Default.Accessibility, contentDescription = null) },
        text = { Text(text = "Accesibilidad") }
    )
}

// Les  voy a ser sincero, no supe como hacer para que fuera un circulo rectangulo
@Preview(showBackground = true)
@Composable
fun AccessibilityButtonPreview() {
    NeupsiproMovilTheme {
        AccessibilityButton(onClick = {})
    }
}