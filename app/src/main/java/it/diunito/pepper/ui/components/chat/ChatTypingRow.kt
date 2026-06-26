package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TypingRow(
    sender: Sender,
    modifier: Modifier = Modifier,
    maxBubbleWidth: Dp = 420.dp
) {
    val (containerColor, contentColor) = bubbleColor(sender)
    val isMessageFromPepper = sender == Sender.PEPPER
    val side = if (isMessageFromPepper) BubbleSide.LEFT else BubbleSide.RIGHT

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (side == BubbleSide.LEFT) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        if (isMessageFromPepper) {
            PepperAvatar()
            Spacer(Modifier.width(8.dp))
            MessageBubble(
                side = BubbleSide.LEFT,
                containerColor = containerColor,
                maxWidth = maxBubbleWidth
            ) { TypingDots(color = contentColor) }
        } else {
            MessageBubble(
                side = BubbleSide.RIGHT,
                containerColor = containerColor,
                maxWidth = maxBubbleWidth
            ) { TypingDots(color = contentColor) }
            Spacer(Modifier.width(8.dp))
            UserAvatar()
        }
    }
}
