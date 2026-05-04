package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.neupsipromovil.presentation.theme.DarkBlue

@Composable
fun AppText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = DarkBlue,
) {
    Text(text = text, style = style.copy(color = color), modifier = modifier)
}