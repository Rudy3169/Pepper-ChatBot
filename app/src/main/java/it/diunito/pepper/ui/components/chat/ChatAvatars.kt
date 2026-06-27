package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.theme.BubbleDarkIncoming
import it.diunito.pepper.ui.theme.BubbleDarkOutgoing

@Composable
fun Avatar (
    modifier: Modifier = Modifier,
    bgColor: Color,
    painter: Painter?=null, // image from resource
    imageVector: ImageVector?=null, // icon preset
    contentTint: Color,
    size: Dp = 32.dp
) {
    val isDark = LocalIsDark.current

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(
                if (isDark) {
                    // Subtle border for definition on dark backgrounds
                    Modifier.border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                } else Modifier
            )
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        when {
            painter != null -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            imageVector != null -> {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = contentTint,
                    modifier = Modifier.size(size * 0.56f)
                )
            }
        }
    }
}


@Composable
fun PepperAvatar(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    val isDark = LocalIsDark.current

    // Muted background in dark mode instead of bright yellow
    val bg = if (isDark) BubbleDarkIncoming.copy(alpha = 0.8f)
             else MaterialTheme.colorScheme.secondary

    Avatar(
        modifier = modifier,
        bgColor = bg,
        painter = painterResource(R.drawable.ic_pepper),
        contentTint = MaterialTheme.colorScheme.onSecondary,
        size = size
    )
}

@Composable
fun UserAvatar(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    val isDark = LocalIsDark.current

    // Muted teal in dark mode instead of bright blue
    val bg = if (isDark) BubbleDarkOutgoing
             else MaterialTheme.colorScheme.primary
    val tint = if (isDark) Color.White.copy(alpha = 0.9f)
               else MaterialTheme.colorScheme.onPrimary

    Avatar(
        modifier = modifier,
        bgColor = bg,
        imageVector = Icons.Filled.Person,
        contentTint = tint,
        size = size
    )
}
