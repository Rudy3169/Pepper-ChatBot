package it.diunito.pepper.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.diunito.pepper.ui.theme.ClientTheme

@Composable
fun ChatMessageRow(
    modifier: Modifier = Modifier,
    msg: ChatMessage,
    maxBubbleWidth: Dp = 420.dp,
    sender: Sender = msg.sender, // true if Pepper, false if user
    hasTail: Boolean = true
) {
    val side = if(sender == Sender.PEPPER) BubbleSide.LEFT else BubbleSide.RIGHT
    val (containerColor, contentColor) = bubbleColor(sender)

    // animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(msg.id) { visible = true }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = if (side == BubbleSide.LEFT) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            val timeString = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(msg.timestamp))

            MessageBubble(
                side = side,
                containerColor = containerColor,
                maxWidth = maxBubbleWidth,
                hasTail = hasTail
            ) {
                androidx.compose.foundation.layout.Column {
                    Text(
                        text = msg.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
                        ),
                        color = contentColor.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

/** PREVIEW **/

@Preview(showBackground = true, name = "Pepper row")
@Composable
private fun PreviewMessageRow_Pepper() {
    ClientTheme {
        ChatMessageRow(
            msg = ChatMessage(
                id = 1,
                text = "Ciao! Come posso aiutarti oggi?",
                sender = Sender.PEPPER
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true, name = "User row")
@Composable
private fun PreviewMessageRow_User() {
    ClientTheme {
        ChatMessageRow(
            msg = ChatMessage(
                id = 2,
                text = "Vorrei una ricetta veloce per pranzo!",
                sender = Sender.USER
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}
