package it.diunito.pepper.ui.components.chat

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TypingDots(
    color: Color,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.dp,
    space: Dp = 6.dp
) {
    val t = rememberInfiniteTransition(label = "typing")
    // dots rhythm
    val s1 = t.animateFloat(
        initialValue = 0.7f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 900
                1.0f at 300 using FastOutSlowInEasing
                0.7f at 900
            },
            RepeatMode.Restart
        ),
        label = "dot1"
    )
    val s2 = t.animateFloat(
        initialValue = 0.7f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 900
                1.0f at 450 using FastOutSlowInEasing
                0.7f at 900
            }
        ),
        label = "dot2"
    )
    val s3 = t.animateFloat(
        initialValue = 0.7f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 900
                1.0f at 600 using FastOutSlowInEasing
                0.7f at 900
            }
        ),
        label = "dot3"
    )

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(dotSize).scale(s1.value).clip(CircleShape).background(color)
        )
        Spacer(Modifier.width(space))
        Box(
            Modifier.size(dotSize).scale(s2.value).clip(CircleShape).background(color)
        )
        Spacer(Modifier.width(space))
        Box(
            Modifier.size(dotSize).scale(s3.value).clip(CircleShape).background(color)
        )
    }
}
