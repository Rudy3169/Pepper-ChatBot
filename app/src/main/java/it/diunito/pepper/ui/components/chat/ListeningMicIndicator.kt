package it.diunito.pepper.ui.components.chat

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientIcons

@Composable
fun ListeningMicIndicator(
    color: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp
) {
    val transition = rememberInfiniteTransition(label = "listening_mic")

    // Pulsing scale animation (calm breathing rhythm)
    val scale by transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    // Breathing alpha / glow animation
    val alpha by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_alpha"
    )

    Box(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = ClientIcons.mic(),
            contentDescription = "Listening...",
            tint = color.copy(alpha = alpha),
            modifier = Modifier
                .size(iconSize)
                .scale(scale)
        )
    }
}
