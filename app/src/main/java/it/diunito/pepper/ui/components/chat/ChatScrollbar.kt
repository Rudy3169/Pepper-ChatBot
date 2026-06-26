package it.diunito.pepper.ui.components.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max


@SuppressLint("FrequentlyChangingValue")
@Composable
fun ChatScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 6.dp,
    thumbMinHeight: Dp = 40.dp,
    thumbRadius: Dp = 3.dp,
    trackAlpha: Float = 0.10f,
    thumbAlpha: Float = 0.55f
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Measure the track height (matches the visible height of the list)
    var trackHeightPx by remember { mutableStateOf(0f) }
    val trackShape = RoundedCornerShape(thumbRadius)

    // LazyColumn layout info
    val layout = listState.layoutInfo
    val viewportPx = (layout.viewportEndOffset - layout.viewportStartOffset).toFloat().coerceAtLeast(1f)
    val visible = layout.visibleItemsInfo
    val totalItems = layout.totalItemsCount

    // not showing scrollbar if not needed or no items
    if (totalItems <= 0 || visible.isEmpty()) {
        Box(
            modifier = modifier
                .width(trackWidth)
                .fillMaxHeight()
                .onGloballyPositioned { trackHeightPx = it.size.height.toFloat() }
        )
        return
    }

    // estimation of average item size (in px)
    val avgItemSizePx = (visible.sumOf { it.size }.toFloat() / visible.size).coerceAtLeast(1f)

    // content total height (in px)
    val totalContentPx = avgItemSizePx * totalItems

    // thumb height (in px), at least thumbMinHeight
    val minThumbPx = with(density) { thumbMinHeight.toPx() }
    val trackHeight = if (trackHeightPx > 0f) trackHeightPx else viewportPx
    val thumbHeightPx = max(minThumbPx, (trackHeight * (viewportPx / totalContentPx)))

    // Offset (px) of items before the first visible one
    val firstIndex = listState.firstVisibleItemIndex
    val firstOffsetPx = listState.firstVisibleItemScrollOffset.toFloat()
    val itemsBeforePx = firstIndex * avgItemSizePx + firstOffsetPx

    // thumb position (px) based on the list state
    val maxThumbTravelPx = max(0f, trackHeight - thumbHeightPx)
    val maxContentTravelPx = max(1f, totalContentPx - viewportPx)
    val thumbTopPxFromState = (itemsBeforePx / maxContentTravelPx) * maxThumbTravelPx

    // drag status and position
    var isDragging by remember { mutableStateOf(false) }
    var thumbTopPx by remember { mutableStateOf(thumbTopPxFromState) }
    // following the list if not dragging
    if (!isDragging) thumbTopPx = thumbTopPxFromState

    val dragState = rememberDraggableState { delta ->
        if (trackHeight <= 0f) return@rememberDraggableState

        isDragging = true
        thumbTopPx = (thumbTopPx + delta).coerceIn(0f, maxThumbTravelPx)

        // content position mapping
        val targetItemsBeforePx = (thumbTopPx / maxThumbTravelPx) * maxContentTravelPx
        val targetIndex = (targetItemsBeforePx / avgItemSizePx).toInt().coerceIn(0, max(0, totalItems - 1))

        scope.launch {
            listState.scrollToItem(targetIndex)
        }
    }

    // list status changes (notably when scrolling), stop dragging
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            thumbTopPx = thumbTopPxFromState
        }
    }

    Box(
        modifier = modifier
            .width(trackWidth)
            .fillMaxHeight()
            .onGloballyPositioned { trackHeightPx = it.size.height.toFloat() }
    ) {
        // Track
        Box(
            Modifier
                .fillMaxSize()
                .alpha(trackAlpha)
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = trackShape
                )
        )

        // Thumb (draggable)
        Box(
            Modifier
                .fillMaxWidth()
                .offset(y = with(density) { thumbTopPx.toDp() })
                .height(with(density) { thumbHeightPx.toDp() })
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = thumbAlpha),
                    shape = trackShape
                )
                .draggable(
                    orientation = Orientation.Vertical,
                    state = dragState,
                    onDragStopped = { isDragging = false }
                )
        )
    }
}
