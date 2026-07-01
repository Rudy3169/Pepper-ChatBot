package it.diunito.pepper.ui.components.overlay

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.AIRblue25
import it.diunito.pepper.ui.theme.AIRyellow25
import it.diunito.pepper.ui.theme.UNITOred

@Composable
fun AnimatedMeshBackground(modifier: Modifier = Modifier) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val infiniteTransition = rememberInfiniteTransition(label = "mesh_bg")

    // Background blob breathing animations (different periods for organic feel)
    val blobScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )
    val blobScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )
    val blobScale3 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob3"
    )

    // Rich multi-stop background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            backgroundColor,
            surfaceVariantColor.copy(alpha = 0.15f),
            backgroundColor
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // Top-end blob (UNITOred tint)
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-60).dp)
                .scale(blobScale1)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            UNITOred.copy(alpha = 0.10f),
                            UNITOred.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Bottom-start blob (AIRblue tint)
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 80.dp)
                .scale(blobScale2)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AIRblue25.copy(alpha = 0.14f),
                            AIRblue25.copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center-start blob (AIRyellow tint)
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-40).dp, y = (-80).dp)
                .scale(blobScale3)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AIRyellow25.copy(alpha = 0.10f),
                            AIRyellow25.copy(alpha = 0.02f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}
