package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neupsipromovil.presentation.common.atoms.ProfileAvatar

private val CardBackground = Color(0xFFFFFFFF)
private val CardBorder     = Color(0xFFE0E0E0)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextSecondary  = Color(0xFF6B7280)

@Composable
fun StaffMemberCard(
    roleTitle: String,
    staffName: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text       = "Mi ${roleTitle.lowercase()}",
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
            modifier   = Modifier.padding(bottom = 8.dp),
        )
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border    = BorderStroke(1.dp, CardBorder),
        ) {
            Row(
                modifier          = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProfileAvatar(image = null, size = 38.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text       = staffName,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary,
                    )
                    Text(
                        text     = roleTitle,
                        fontSize = 12.sp,
                        color    = TextSecondary,
                    )
                }
            }
        }
    }
}