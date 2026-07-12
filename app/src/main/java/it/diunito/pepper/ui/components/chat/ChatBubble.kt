package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.theme.BubbleDarkIncoming
import it.diunito.pepper.ui.theme.BubbleDarkOutgoing
import it.diunito.pepper.ui.theme.BubbleLightIncoming
import it.diunito.pepper.ui.theme.BubbleLightOutgoing
import it.diunito.pepper.ui.theme.OnBubbleDarkIncoming
import it.diunito.pepper.ui.theme.OnBubbleDarkOutgoing
import it.diunito.pepper.ui.theme.OnBubbleLightIncoming
import it.diunito.pepper.ui.theme.OnBubbleLightOutgoing

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BubbleShape(
    private val side: BubbleSide,
    private val hasTail: Boolean
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = with(density) { 12.dp.toPx() }
        val tailWidth = with(density) { 8.dp.toPx() }
        val tailHeight = with(density) { 8.dp.toPx() }
        
        val path = Path()

        if (side == BubbleSide.LEFT) {
            // Main bubble body
            val bodyRect = RoundRect(
                left = tailWidth,
                top = 0f,
                right = size.width,
                bottom = size.height,
                topLeftCornerRadius = androidx.compose.ui.geometry.CornerRadius(if (hasTail) 0f else radius),
                topRightCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                bottomRightCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                bottomLeftCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius)
            )
            path.addRoundRect(bodyRect)

            if (hasTail) {
                // Tail on the top left
                path.moveTo(tailWidth, 0f)
                path.lineTo(0f, 0f)
                path.lineTo(tailWidth, tailHeight)
                path.close()
            }
        } else {
            // Main bubble body
            val bodyRect = RoundRect(
                left = 0f,
                top = 0f,
                right = size.width - tailWidth,
                bottom = size.height,
                topLeftCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                topRightCornerRadius = androidx.compose.ui.geometry.CornerRadius(if (hasTail) 0f else radius),
                bottomRightCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                bottomLeftCornerRadius = androidx.compose.ui.geometry.CornerRadius(radius)
            )
            path.addRoundRect(bodyRect)

            if (hasTail) {
                // Tail on the top right
                path.moveTo(size.width - tailWidth, 0f)
                path.lineTo(size.width, 0f)
                path.lineTo(size.width - tailWidth, tailHeight)
                path.close()
            }
        }
        
        return Outline.Generic(path)
    }
}

private fun getBubbleShape(side: BubbleSide, hasTail: Boolean): Shape {
    return BubbleShape(side, hasTail)
}

// Dedicated chat color logic with separate dark-mode palette
@Composable
fun bubbleColor(sender: Sender): Pair<Color, Color> {
    val isDark = LocalIsDark.current
    val isFromPepper = sender == Sender.PEPPER

    val container = when {
        isDark && isFromPepper  -> BubbleDarkIncoming
        isDark && !isFromPepper -> BubbleDarkOutgoing
        !isDark && isFromPepper -> BubbleLightIncoming
        else                    -> BubbleLightOutgoing
    }
    val content = when {
        isDark && isFromPepper  -> OnBubbleDarkIncoming
        isDark && !isFromPepper -> OnBubbleDarkOutgoing
        !isDark && isFromPepper -> OnBubbleLightIncoming
        else                    -> OnBubbleLightOutgoing
    }
    return container to content
}

@Composable
fun MessageBubble(
    side: BubbleSide,
    containerColor: Color,
    maxWidth: Dp = 420.dp,
    hasTail: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = LocalIsDark.current
    val shape = getBubbleShape(side, hasTail)

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape,
        elevation = if (isDark) {
            CardDefaults.cardElevation(defaultElevation = 0.dp)
        } else {
            CardDefaults.cardElevation(defaultElevation = 1.dp)
        },
        modifier = Modifier
            .widthIn(max = maxWidth)
            .then(
                if (isDark) {
                    // Subtle border in dark mode for depth
                    Modifier.border(
                        width = 0.5.dp,
                        color = Color.White.copy(alpha = 0.06f),
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
    ) {
        val startPadding = if (side == BubbleSide.LEFT) 22.dp else 14.dp
        val endPadding = if (side == BubbleSide.RIGHT) 22.dp else 14.dp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = startPadding, end = endPadding, top = 10.dp, bottom = 10.dp),
            content = content
        )
    }
}
