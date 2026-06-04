import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val AccentBlue     = Color(0xFF3F51B5)
private val AccentBlueSoft = Color(0xFFE8EAF6)
private val SurfaceWhite   = Color(0xFFFFFFFF)
private val TextSecondary  = Color(0xFF6B7280)

@Composable
fun CreatePostImagePicker(
    imagenUri: Uri?,
    enabled:   Boolean,
    onPick:    () -> Unit,
    onCamera:  () -> Unit,
    onRemove:  () -> Unit,
) {
    AnimatedVisibility(
        visible = imagenUri == null,
        enter   = fadeIn(tween(300)),
        exit    = fadeOut(tween(200)),
    ) {
        ImagePlaceholder(enabled = enabled, onGallery = onPick, onCamera = onCamera)
    }

    AnimatedVisibility(
        visible = imagenUri != null,
        enter   = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
        exit    = fadeOut(tween(200)) + scaleOut(tween(200)),
    ) {
        ImagePreview(uri = imagenUri, enabled = enabled, onRemove = onRemove)
    }
}

@Composable
private fun ImagePlaceholder(enabled: Boolean, onGallery: () -> Unit, onCamera: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceWhite)
            .border(width = 1.5.dp, color = Color(0xFFD0D4F0), shape = RoundedCornerShape(14.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier         = Modifier.size(52.dp).clip(CircleShape).background(AccentBlueSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector        = Icons.Outlined.AddPhotoAlternate,
                contentDescription = null,
                tint               = AccentBlue,
                modifier           = Modifier.size(26.dp),
            )
        }

        Text(
            text       = "Añade una imagen",
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Color(0xFF1A1A2E),
        )
        Text(
            text     = "Máximo 5 MB",
            fontSize = 11.sp,
            color    = TextSecondary,
        )

        Row(
            modifier            = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Galería
            OutlinedButton(
                onClick  = onGallery,
                enabled  = enabled,
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = AccentBlue),
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.AddPhotoAlternate,
                    contentDescription = null,
                    modifier           = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Galería", fontSize = 13.sp)
            }

            // Cámara
            Button(
                onClick  = onCamera,
                enabled  = enabled,
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint               = Color.White,
                    modifier           = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Cámara", fontSize = 13.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun ImagePreview(uri: Uri?, enabled: Boolean, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(14.dp)),
    ) {
        AsyncImage(
            model              = uri,
            contentDescription = "Imagen seleccionada",
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize(),
        )
        IconButton(
            onClick  = onRemove,
            enabled  = enabled,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.55f)),
        ) {
            Icon(
                imageVector        = Icons.Default.Close,
                contentDescription = "Quitar imagen",
                tint               = Color.White,
                modifier           = Modifier.size(18.dp),
            )
        }
    }
}