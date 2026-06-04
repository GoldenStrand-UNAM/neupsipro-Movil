package com.example.neupsipromovil.presentation.screens.forum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.neupsipromovil.domain.model.ForumPost
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val CardBackground   = Color(0xFFEEF0FB)
private val AccentBlue       = Color(0xFF3F51B5)
private val AccentBlueSoft   = Color(0xFFE8EAF6)
private val AvatarBackground = Color(0xFFD0D4F0)
private val TextPrimary      = Color(0xFF1A1A2E)
private val TextSecondary    = Color(0xFF6B7280)

@Composable
fun ForumPostCard(
    post: ForumPost,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "arrow_rotation",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness    = Spring.StiffnessMediumLow,
                ),
            ),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(avatarUrl = post.avatarUrl, author = post.author)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = post.author,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = TextPrimary,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                    )
                    Text(
                        text     = formatRelativeDate(post.date),
                        fontSize = 12.sp,
                        color    = TextSecondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text       = post.title,
                fontWeight = FontWeight.Bold,
                fontSize   = 17.sp,
                color      = TextPrimary,
                maxLines   = if (expanded) Int.MAX_VALUE else 2,
                overflow   = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text      = post.content,
                fontSize  = 14.sp,
                color     = TextSecondary,
                maxLines  = if (expanded) Int.MAX_VALUE else 3,
                overflow  = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                lineHeight = 20.sp,
            )

            AnimatedVisibility(
                visible = expanded && !post.imageUrl.isNullOrBlank(),
                enter   = fadeIn(tween(300)) + expandVertically(tween(400)),
                exit    = fadeOut(tween(200)) + shrinkVertically(tween(300)),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    AsyncImage(
                        model              = post.imageUrl,
                        contentDescription = "Imagen del post",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp)),
                    )
                }
            }

            val hasImage     = !post.imageUrl.isNullOrBlank()
            val hasLongText  = post.title.length > 60 || post.content.length > 100

            if (hasImage || hasLongText) {
                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(color = Color(0xFFDDE0F0), thickness = 1.dp)

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null,
                            onClick           = { expanded = !expanded },
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Text(
                        text       = if (expanded) "Ver menos" else "Ver más",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = AccentBlue,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector        = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint               = AccentBlue,
                        modifier           = Modifier
                            .size(18.dp)
                            .rotate(arrowRotation),
                    )
                }
            }
        }
    }
}

@Composable
private fun UserAvatar(avatarUrl: String?, author: String) {
    Box(
        modifier         = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(AvatarBackground),
        contentAlignment = Alignment.Center,
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model              = avatarUrl,
                contentDescription = "Avatar de $author",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(44.dp)
                    .clip(CircleShape),
            )
        } else {
            Text(
                text       = author.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = AccentBlue,
            )
        }
    }
}

private fun formatRelativeDate(isoDate: String): String {
    return try {
        val instant     = Instant.parse(isoDate)
        val now         = Instant.now()
        val diffSeconds = now.epochSecond - instant.epochSecond
        when {
            diffSeconds < 60      -> "Justo ahora"
            diffSeconds < 3_600   -> "Hace ${diffSeconds / 60} min"
            diffSeconds < 86_400  -> "Hace ${diffSeconds / 3_600} h"
            diffSeconds < 604_800 -> "Hace ${diffSeconds / 86_400} días"
            else -> DateTimeFormatter
                .ofPattern("dd MMM yyyy", Locale("es", "MX"))
                .withZone(ZoneId.systemDefault())
                .format(instant)
        }
    } catch (e: Exception) {
        isoDate
    }
}