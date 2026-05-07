package com.example.neupsipromovil.presentation.common.molecules.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.atoms.ProfileAvatar
import com.example.neupsipromovil.presentation.common.atoms.StatusTag
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun ProfileHeader(
    fullName: String,
    stage: String,
    image: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileAvatar(image = image, size = 100.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        StatusTag(status = stage)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    NeupsiproMovilTheme {
        ProfileHeader(
            fullName = "John Doe",
            stage = "Activo",
            image = null
        )
    }
}