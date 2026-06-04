package com.example.neupsipromovil.presentation.screens.forum.PostForum


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentBlue = Color(0xFF3F51B5)

@Composable
fun CreatePostSubmitButton(
    isLoading: Boolean,
    onClick:   () -> Unit,
    enabled: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue   = if (isLoading) 0.97f else 1f,
        animationSpec = tween(150),
        label         = "btn_scale",
    )

    Button(
        onClick  = onClick,
        enabled  = !isLoading,
        shape    = RoundedCornerShape(16.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor         = AccentBlue,
            disabledContainerColor = AccentBlue.copy(alpha = 0.6f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color       = Color.White,
                modifier    = Modifier.size(22.dp),
                strokeWidth = 2.5.dp,
            )
        } else {
            Icon(
                imageVector        = Icons.Outlined.Send,
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Publicar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
