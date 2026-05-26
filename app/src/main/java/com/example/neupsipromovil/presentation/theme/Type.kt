@file:Suppress("ktlint:standard:filename")

package com.example.neupsipromovil.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppTypography {
    val titleM =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 40.sp,
            lineHeight = 42.sp,
            fontWeight = FontWeight.Normal,
        )
    val bodyBase =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
        )
    val labelBase =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            lineHeight = 42.sp,
            fontWeight = FontWeight.Normal,
        )
    val caption =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Normal,
        )

    val buttonLabel =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Medium,
        )
}
