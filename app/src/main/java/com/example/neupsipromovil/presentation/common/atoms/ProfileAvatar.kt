package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.neupsiproMovil.R
import com.example.neupsipromovil.presentation.theme.Blue02

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileAvatar(
    image: String?,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = Blue02,
        tonalElevation = 2.dp,
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .listener(
                        onStart = { println("DEBUG_S3: Iniciando carga de imagen...") },
                        onSuccess = { _, _ -> println("DEBUG_S3: ¡Imagen cargada con éxito!") },
                        onError = { _, result ->
                            println("DEBUG_S3: Error al cargar imagen. Causa: ${result.throwable}")
                        },
                    ).build(),
            contentDescription = "Foto de Perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.default_avatar),
            error = painterResource(R.drawable.default_avatar),
        )
    }
}
