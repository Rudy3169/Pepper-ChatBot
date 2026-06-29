package it.diunito.pepper.ui.components.overlay

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.AIRblue
import it.diunito.pepper.ui.theme.AIRyellow
import it.diunito.pepper.ui.theme.ClientIcons


// ── Track dimensions ──
private val TrackWidth = 52.dp
private val TrackHeight = 28.dp
private val ThumbSize = 22.dp
private val ThumbTravel = TrackWidth - TrackHeight  // horizontal travel distance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeToggle(
    isDark: Boolean,
    onToggle: () -> Unit,
    onResetToSystem: (() -> Unit)? = null
) {
    // ── Animated values ──
    val thumbOffset by animateDpAsState(
        targetValue = if (isDark) ThumbTravel else 0.dp,
        animationSpec = tween(320, easing = EaseInOutCubic),
        label = "thumbOffset"
    )

    // Track gradient endpoints
    val trackStartColor by animateColorAsState(
        targetValue = if (isDark) Color(0xFF1A1F35) else AIRyellow.copy(alpha = 0.35f),
        animationSpec = tween(400),
        label = "trackStart"
    )
    val trackEndColor by animateColorAsState(
        targetValue = if (isDark) Color(0xFF2D2B55) else AIRblue.copy(alpha = 0.18f),
        animationSpec = tween(400),
        label = "trackEnd"
    )

    // Thumb color
    val thumbColor by animateColorAsState(
        targetValue = if (isDark) Color(0xFF2D3A4A) else Color.White,
        animationSpec = tween(350),
        label = "thumbColor"
    )

    // Icon tint
    val iconTint by animateColorAsState(
        targetValue = if (isDark) Color(0xFFE8D44D) else AIRyellow,
        animationSpec = tween(350),
        label = "iconTint"
    )

    // Icon rotation (sun spins slightly, moon tilts)
    val iconRotation by animateFloatAsState(
        targetValue = if (isDark) -30f else 0f,
        animationSpec = tween(400, easing = EaseInOutCubic),
        label = "iconRotation"
    )

    // ── Track (the outer pill) ──
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .shadow(
                elevation = if (isDark) 4.dp else 2.dp,
                shape = RoundedCornerShape(999.dp),
                ambientColor = if (isDark) Color(0xFF6C63FF).copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f),
                spotColor = if (isDark) Color(0xFF6C63FF).copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(999.dp))
            .drawBehind {
                // Gradient track fill
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(trackStartColor, trackEndColor)
                    ),
                    cornerRadius = CornerRadius(size.height, size.height)
                )
                // Subtle inner border for depth
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.06f else 0.5f),
                            Color.Black.copy(alpha = if (isDark) 0.15f else 0.04f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height)
                    ),
                    cornerRadius = CornerRadius(size.height, size.height)
                )
            }
            .combinedClickable(
                onClick = onToggle,
                onLongClick = { onResetToSystem?.invoke() }
            )
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // ── Thumb (the sliding circle) ──
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .shadow(
                    elevation = 3.dp,
                    shape = CircleShape,
                    ambientColor = if (isDark) Color(0xFFE8D44D).copy(alpha = 0.2f) else AIRyellow.copy(alpha = 0.3f)
                )
                .clip(CircleShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            // Sun or Moon icon
            Icon(
                painter = if (isDark) ClientIcons.theme_dark() else ClientIcons.theme_light(),
                contentDescription = if (isDark) "Dark theme" else "Light theme",
                tint = iconTint,
                modifier = Modifier
                    .size(14.dp)
                    .rotate(iconRotation)
            )
        }
    }
}
