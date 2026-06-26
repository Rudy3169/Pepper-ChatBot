package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.components.overlay.LocalIsDark

// pick the bubble side based on who is the sender
private fun getBubbleShape(side: BubbleSide) = when (side) {
    BubbleSide.LEFT -> RoundedCornerShape(topStart = 6.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 18.dp)
    BubbleSide.RIGHT -> RoundedCornerShape(topStart = 18.dp, topEnd = 6.dp, bottomEnd = 18.dp, bottomStart = 18.dp)
}

// Chat balloon component style, color and shape
@Composable
fun bubbleColor(sender: Sender): Pair<Color,Color>{
    val isDark = LocalIsDark.current
    val isMessageFromPepper = sender == Sender.PEPPER
    val lightContainer = if (isMessageFromPepper) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val darkContainer = if (isMessageFromPepper) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val container = if (isDark) darkContainer else lightContainer
    val lightContent = if (isMessageFromPepper) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
    val darkContent = if (isMessageFromPepper) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
    val content = if (isDark) darkContent else lightContent

    return container to content
}

@Composable
fun MessageBubble(
    side: BubbleSide,
    containerColor: Color,
    maxWidth: Dp = 420.dp,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = getBubbleShape(side),
        modifier = Modifier.widthIn(max = maxWidth)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            content = content
        )
    }
}
