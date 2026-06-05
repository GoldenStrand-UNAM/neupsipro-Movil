package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.atoms.DateBadge
import com.example.neupsipromovil.presentation.common.molecules.profile.AppointmentContent

private val CardBackground = Color(0xFFFFFFFF)
private val CardBorder     = Color(0xFFE0E0E0)

@Composable
fun AppointmentCard(
    month: String,
    day: String,
    title: String,
    time: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border    = BorderStroke(1.dp, CardBorder),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DateBadge(month = month, day = day)
            Spacer(modifier = Modifier.width(12.dp))
            AppointmentContent(title = title, time = time)
        }
    }
}