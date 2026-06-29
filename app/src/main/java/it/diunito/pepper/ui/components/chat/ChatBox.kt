package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
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
    // The background is now handled by the parent Surface in EngageScreen
    // to provide a clean, modern card look without the noisy tiled pattern.

//    val messages by viewModel.chat.observeAsState(emptyList())
    val listState = rememberLazyListState()

    // Recycler view for the messages
    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 12.dp)
        ) {
            items(items = messages, key = { it.id }) { msg ->
                ChatMessageRow(
                    msg = msg,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // show dots animation while processing messages
            if (showUserTyping) {
                item { TypingRow(sender = Sender.USER, modifier = Modifier.fillMaxWidth()) }
            }
            if (showPepperTyping) {
                item { TypingRow(sender = Sender.PEPPER, modifier = Modifier.fillMaxWidth()) }
            }
        }
//        ChatScrollbar(
//            listState = listState,
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .padding(end = 6.dp)
//        )
    }

    LaunchedEffect(Unit) {
        snapshotFlow { // react to changes in the messages list or typing indicators
            Triple(messages.size, showUserTyping, showPepperTyping)
        }.collectLatest {
            val total = listState.layoutInfo.totalItemsCount
            if (total > 0) {
                listState.scrollToItem(total - 1) // scroll to the last item
            }
        }
    }
}
