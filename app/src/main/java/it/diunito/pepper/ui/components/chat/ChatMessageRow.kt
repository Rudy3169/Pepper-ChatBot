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
import it.diunito.pepper.ui.theme.ClientTheme

@Composable
fun ChatMessageRow(
    modifier: Modifier = Modifier,
    msg: ChatMessage,
    maxBubbleWidth: Dp = 420.dp,
    sender: Sender = msg.sender // true if Pepper, false if user
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
            if (sender == Sender.PEPPER) {
                PepperAvatar()
                Spacer(Modifier.width(8.dp))

                MessageBubble(
                    side = BubbleSide.LEFT,
                    containerColor = containerColor,
                    maxWidth = maxBubbleWidth
                ) {
                    Text(
                        text = msg.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
            } else {
                MessageBubble(
                    side = BubbleSide.RIGHT,
                    containerColor = containerColor,
                    maxWidth = maxBubbleWidth
                ) {
                    Text(
                        text = msg.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
                Spacer(Modifier.width(8.dp))
                UserAvatar()
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
