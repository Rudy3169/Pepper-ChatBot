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

// Asymmetric bubble shapes for a conversational UI: differentiated corner radii for message origin
private fun getBubbleShape(side: BubbleSide) = when (side) {
    BubbleSide.LEFT -> RoundedCornerShape(
        topStart = 4.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp
    )
    BubbleSide.RIGHT -> RoundedCornerShape(
        topStart = 20.dp, topEnd = 4.dp, bottomEnd = 20.dp, bottomStart = 20.dp
    )
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
    content: @Composable RowScope.() -> Unit
) {
    val isDark = LocalIsDark.current
    val shape = getBubbleShape(side)

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            content = content
        )
    }
}
