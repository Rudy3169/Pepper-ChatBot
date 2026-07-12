package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import kotlinx.coroutines.launch
import it.diunito.pepper.ui.theme.ClientPictures
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    showUserTyping: Boolean,
    showPepperTyping: Boolean,

    ) {
    val isDark = LocalIsDark.current

//    val messages by viewModel.chat.observeAsState(emptyList())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToBottom by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 50
        }
    }

    // Recycler view for the messages
    Box(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .widthIn(max = 860.dp)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 8.dp)
            ) {
                // In reverseLayout, the first items are at the BOTTOM.
                // So we put the typing indicators first.
                if (showPepperTyping) {
                    item { TypingRow(sender = Sender.PEPPER, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) }
                }
                if (showUserTyping) {
                    item { TypingRow(sender = Sender.USER, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) }
                }
                
                // Then the messages in reversed order
                val reversedMessages = messages.reversed()
                itemsIndexed(items = reversedMessages, key = { _, it -> it.id }) { index, msg ->
                    val olderMsg = if (index + 1 < reversedMessages.size) reversedMessages[index + 1] else null
                    val hasTail = olderMsg?.sender != msg.sender

                    val newerMsg = if (index - 1 >= 0) reversedMessages[index - 1] else null
                    val isLastInGroup = newerMsg?.sender != msg.sender
                    val bottomPadding = if (isLastInGroup) 6.dp else 0.dp

                    ChatMessageRow(
                        msg = msg,
                        hasTail = hasTail,
                        modifier = Modifier.fillMaxWidth().padding(bottom = bottomPadding)
                    )
                }
            }

            // WhatsApp-style Scroll to bottom button
            AnimatedVisibility(
                visible = showScrollToBottom,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isDark) Color(0xFF202C33) else Color.White, // Background color
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Scroll to bottom",
                            tint = if (isDark) Color(0xFF8696A0) else Color(0xFF667781), // Arrow color
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(messages.size, showUserTyping, showPepperTyping) {
        // With reverseLayout, index 0 is always the exact bottom of the chat!
        val total = messages.size + (if (showUserTyping) 1 else 0) + (if (showPepperTyping) 1 else 0)
        if (total > 0) {
            listState.animateScrollToItem(0)
        }
    }
}
