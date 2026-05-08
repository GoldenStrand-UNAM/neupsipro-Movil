package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.molecules.profile.InfoRow
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun ClinicalInfoCard(
    age: Int,
    unitEntryDate: String,
    neuroEntryDate: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            InfoRow(icon = Icons.Default.Cake, label = "Edad", value = "$age años")
            InfoRow(icon = Icons.AutoMirrored.Filled.Login, label = "Fecha de Ingreso unidad:", value = unitEntryDate)
            InfoRow(icon = Icons.Default.MedicalServices, label = "Fecha de Ingreso neuropsicólogia:", value = neuroEntryDate)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClinicalInfoCardPreview() {
    NeupsiproMovilTheme {
        ClinicalInfoCard(
            age = 52,
            unitEntryDate = "2022-01-01",
            neuroEntryDate = "2020-01-01",
            modifier = Modifier.padding(16.dp)
        )
    }
}