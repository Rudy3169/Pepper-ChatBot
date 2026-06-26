package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun Avatar (
    modifier: Modifier = Modifier,
    bgColor: Color,
    painter: Painter?=null, // image from resource
    imageVector: ImageVector?=null, // icon preset
    contentTint: Color,
    size:Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
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
    Avatar(
        modifier = modifier,
        bgColor = MaterialTheme.colorScheme.secondary,
        painter = painterResource(R.drawable.ic_pepper),
        contentTint = MaterialTheme.colorScheme.onSecondary,
        size = size
    )
}

@Composable
fun UserAvatar(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Avatar(
        modifier = modifier,
        bgColor = MaterialTheme.colorScheme.primary,
        imageVector = Icons.Filled.Person,
        contentTint = MaterialTheme.colorScheme.onPrimary,
        size = size
    )
}

