package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderOrganismo(
    modifier: Modifier = Modifier,
    title: String = "Titulo del Header",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF3F51B5))
                .padding(vertical = 96.dp)
                .padding(horizontal = 136.dp),
    )
    {
        Text(
            text = title,
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderOrganismoPreview() {
    HeaderOrganismo(
        title = "Titulo",
    )

}
