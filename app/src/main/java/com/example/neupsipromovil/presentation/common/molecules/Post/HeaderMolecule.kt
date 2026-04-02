package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeaderMolecule(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun PostHeaderPreview() {
    HeaderMolecule()
}
