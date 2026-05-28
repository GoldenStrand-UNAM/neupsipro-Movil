package com.example.neupsipromovil.presentation.common.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.atoms.AppIcon
import com.example.neupsipromovil.presentation.common.atoms.AppText
import com.example.neupsipromovil.presentation.common.atoms.IconSize
import com.example.neupsipromovil.presentation.theme.AppTypography
import com.example.neupsipromovil.presentation.theme.DarkBlue
import com.example.neupsipromovil.presentation.theme.LightBlue

@Suppress("ktlint:standard:function-naming")
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
) {
    Row(
        modifier =
            modifier
                .widthIn(max = 167.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightBlue)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
    ) {
        leadingIcon?.let {
            AppIcon(
                imageVector = it,
                contentDescription = null,
                size = IconSize.Medium,
                tint = DarkBlue,
            )
        }
        AppText(text = text, style = AppTypography.buttonLabel, color = DarkBlue)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun SecondaryButtonPreview() {
    SecondaryButton(
        text = "Accesibilidad",
        onClick = {},
        leadingIcon = Icons.Default.Accessibility,
    )
}
