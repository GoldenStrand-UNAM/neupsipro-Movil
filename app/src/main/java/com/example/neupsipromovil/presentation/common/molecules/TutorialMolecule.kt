package com.example.neupsipromovil.presentation.common.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.app.neupsiproMovil.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun TutorialMolecula(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .width(122.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFC5CAE9))
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icono_atomo_tutorial),
            contentDescription = "Tutorial",
            modifier =
                Modifier
                    .width(16.dp)
                    .height(24.dp),
            tint = Color(0xff002B7A),
        )
        Text(
            text = "Tutorial",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(16.dp),
            style =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.15.sp,
                    color = Color(0xFF002B7A),
                    textAlign = Center,
                ),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun TutorialMoleculaPreview() {
    TutorialMolecula()
}
