package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.theme.Grey

object IconSize {
    val Small: Dp = 16.dp
    val Medium: Dp = 20.dp
    val Large: Dp = 24.dp
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = IconSize.Medium,
    tint: Color = Grey,
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = IconSize.Medium,
    tint: Color = Grey,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}
