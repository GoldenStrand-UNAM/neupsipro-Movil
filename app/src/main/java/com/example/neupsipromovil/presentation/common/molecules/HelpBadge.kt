package com.example.neupsipromovil.presentation.common.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.atoms.AppIcon
import com.example.neupsipromovil.presentation.common.atoms.IconSize
import com.example.neupsipromovil.presentation.theme.Gold
import com.example.neupsipromovil.presentation.theme.White

@Composable
fun HelpBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(51.dp)
            .clip(CircleShape)
            .background(Gold)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                imageVector = Icons.Default.QuestionMark,
                contentDescription = "Ayuda",
                size = IconSize.Small,
                tint = Gold,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF3F51B5)
@Composable
private fun HelpBadgePreview() { HelpBadge(onClick = {}) }