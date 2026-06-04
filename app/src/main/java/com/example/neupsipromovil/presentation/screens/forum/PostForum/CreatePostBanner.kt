package com.example.neupsipromovil.presentation.screens.forum.PostForum


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentBlue = Color(0xFF3F51B5)

@Composable
fun CreatePostBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AccentBlue)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Column {
            Text(
                text          = "FORO NEUPSI- PRO",
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Bold,
                color         = Color.White.copy(alpha = 0.8f),
                letterSpacing = 1.5.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = "Comparte conocimiento con la comunidad",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = Color.White,
            )
        }
    }
}
