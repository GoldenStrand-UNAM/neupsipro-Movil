package com.example.neupsipromovil.presentation.screens.forum.publiDetail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentBlue = Color(0xFF3F51B5)

@Composable
fun ForumLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = AccentBlue,
            modifier = Modifier.semantics {
                contentDescription = "Cargando"
            },
        )
    }
}

@Composable
fun ForumErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = Icons.Outlined.WifiOff,
            contentDescription = null,
            tint               = Color(0xFFAAAAAA),
            modifier           = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text       = "Algo salió mal",
            fontSize   = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Color(0xFF444444),
            textAlign  = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text      = message,
            fontSize  = 14.sp,
            color     = Color(0xFF888888),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
        ) {
            Text("Reintentar", color = Color.White)
        }
    }
}

@Composable
fun ForumEmptyState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.SentimentDissatisfied,
            contentDescription = null,
            tint = Color(0xFFAAAAAA),
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (query.isBlank()) "No hay publicaciones"
            else "Sin resultados",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF444444),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (query.isBlank()) "Sé el primero en publicar algo en el foro."
            else "No encontramos publicaciones para \"$query\".",
            fontSize = 14.sp,
            color = Color(0xFF888888),
            textAlign = TextAlign.Center,
        )
    }


}