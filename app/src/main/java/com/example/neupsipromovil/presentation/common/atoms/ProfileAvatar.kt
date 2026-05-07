package com.example.neupsipromovil.presentation.common.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.R
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme

@Composable
fun ProfileAvatar(
    image: String?,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        AsyncImage(
            model = image ?: R.drawable.default_avatar,
            contentDescription = "Foto de Perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.default_avatar),
            error = painterResource(R.drawable.default_avatar)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    NeupsiproMovilTheme {
        ProfileAvatar(image = null)
    }
}