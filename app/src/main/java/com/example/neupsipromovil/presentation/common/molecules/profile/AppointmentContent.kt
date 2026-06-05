package com.example.neupsipromovil.presentation.common.molecules.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF6B7280)
private val AccentBlue    = Color(0xFF3F51B5)

@Composable
fun AppointmentContent(
    title: String,
    time: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(start = 12.dp)) {
        Text(
            text       = title,
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Default.AccessTime,
                contentDescription = null,
                modifier           = Modifier.size(14.dp),
                tint               = TextSecondary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text     = time,
                fontSize = 13.sp,
                color    = TextSecondary,
            )
        }
    }
}