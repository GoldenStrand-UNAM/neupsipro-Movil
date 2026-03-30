package com.example.neupsipromovil.presentation.common.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neupsipromovil.R

@Composable
fun ActividadesMolecula(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                modifier
                    .width(56.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    modifier
                        .width(56.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icono_atomo_actividades),
                    contentDescription = "Actividades",
                    modifier =
                        modifier
                            .width(24.dp)
                            .height(22.dp),
                    tint = Color.Black,
                )
            }
        }
        Text(
            text = "Actividades",
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(16.dp),
            style =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFF49454F),
                    textAlign = Center,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActividadesMoleculaPreview() {
    ActividadesMolecula()
}
