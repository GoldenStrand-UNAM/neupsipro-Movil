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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neupsipromovil.presentation.common.atoms.AppIcon
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
    var fontSizeState by remember(text) { mutableStateOf(AppTypography.buttonLabel.fontSize) }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    Row(
        modifier =
            modifier
                .widthIn(max = 167.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightBlue)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
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
        Text(
            text = text,
            modifier = Modifier.graphicsLayer(alpha = if (readyToDraw) 1f else 0f),
            style = AppTypography.buttonLabel.copy(fontSize = fontSizeState),
            color = DarkBlue,
            maxLines = 1,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    if (fontSizeState > 10.sp) {
                        fontSizeState = fontSizeState * 0.9f
                    } else {
                        readyToDraw = true
                    }
                } else {
                    readyToDraw = true
                }
            }
        )
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
