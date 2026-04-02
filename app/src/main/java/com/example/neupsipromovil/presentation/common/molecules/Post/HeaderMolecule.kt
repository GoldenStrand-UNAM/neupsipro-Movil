package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderMolecule(
    modifier: Modifier = Modifier,
    title: String = "Username",
    avatarLetter: String = "U",
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(color = Color.Transparent)
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC5CAE9)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = avatarLetter.take(1).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002B7A),
            )
        }

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A2E),
            modifier = Modifier.padding(start = 12.dp),
        )

        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PostHeaderPreview() {
    HeaderMolecule(title = "Username", avatarLetter = "U")
}
