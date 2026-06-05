package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neupsipromovil.presentation.common.molecules.profile.InfoRow

private val CardBackground = Color(0xFFFFFFFF)
private val CardBorder     = Color(0xFFE0E0E0)
private val TextPrimary    = Color(0xFF1A1A2E)

@Composable
fun ClinicalInfoCard(
    age: Int,
    unitEntryDate: String,
    neuroEntryDate: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border    = BorderStroke(1.dp, CardBorder),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text       = "Información Personal",
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                modifier   = Modifier.padding(bottom = 8.dp),
            )
            InfoRow(icon = Icons.Default.Cake, label = "Edad", value = "$age años")
            InfoRow(icon = Icons.AutoMirrored.Filled.Login, label = "Ingreso unidad", value = unitEntryDate)
            InfoRow(icon = Icons.Default.MedicalServices, label = "Ingreso neuropsicología", value = neuroEntryDate)
        }
    }
}