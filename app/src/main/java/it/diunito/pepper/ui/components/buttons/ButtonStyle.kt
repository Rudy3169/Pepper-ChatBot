package it.diunito.pepper.ui.components.buttons

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ════════════════════════════════════════════════
// Sizing tokens
// ════════════════════════════════════════════════
val ButtonFixedWidth: Dp = 260.dp
val ButtonFixedHeight: Dp = 60.dp
val ButtonIconSize: Dp = 20.dp
val ButtonIconSpacing: Dp = 10.dp
val ButtonMinHeight: Dp = 48.dp
val ButtonMinWidth: Dp = 48.dp

// ════════════════════════════════════════════════
// Color palette — used by drawBehind in AppButton
// ════════════════════════════════════════════════
data class AppButtonColors(
    val fill: Color,        // main background
    val fillPressed: Color, // tint when pressed
    val border: Color,      // subtle outline / bottom edge
    val content: Color,     // text + icon
    val glow: Color         // ambient shadow tint
)

@Composable
fun AppButtonColorsBlue(): AppButtonColors {
    val isDark = isSystemInDarkTheme()
    return AppButtonColors(
        fill = MaterialTheme.colorScheme.primary,
        fillPressed = MaterialTheme.colorScheme.primary.copy(alpha = .82f),
        border = if (isDark) Color.White.copy(alpha = .10f) else MaterialTheme.colorScheme.primary.copy(alpha = .45f),
        content = MaterialTheme.colorScheme.onPrimary,
        glow = MaterialTheme.colorScheme.primary.copy(alpha = .30f)
    )
}

@Composable
fun AppButtonColorsYellow(): AppButtonColors {
    val isDark = isSystemInDarkTheme()
    return AppButtonColors(
        fill = MaterialTheme.colorScheme.secondary,
        fillPressed = MaterialTheme.colorScheme.secondary.copy(alpha = .82f),
        border = if (isDark) Color.White.copy(alpha = .10f) else MaterialTheme.colorScheme.secondary.copy(alpha = .40f),
        content = MaterialTheme.colorScheme.onSecondary,
        glow = MaterialTheme.colorScheme.secondary.copy(alpha = .25f)
    )
}

@Composable
fun AppButtonColorsGray(): AppButtonColors {
    val isDark = isSystemInDarkTheme()
    return AppButtonColors(
        fill = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5f)
               else MaterialTheme.colorScheme.surfaceVariant,
        fillPressed = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .65f),
        border = if (isDark) Color.White.copy(alpha = .06f) else MaterialTheme.colorScheme.outline.copy(alpha = .18f),
        content = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .65f),
        glow = Color.Transparent
    )
}

@Composable
fun AppButtonColorsRed(): AppButtonColors {
    val isDark = isSystemInDarkTheme()
    return AppButtonColors(
        fill = MaterialTheme.colorScheme.error,
        fillPressed = MaterialTheme.colorScheme.error.copy(alpha = .82f),
        border = if (isDark) Color.White.copy(alpha = .10f) else MaterialTheme.colorScheme.error.copy(alpha = .40f),
        content = MaterialTheme.colorScheme.onError,
        glow = MaterialTheme.colorScheme.error.copy(alpha = .30f)
    )
}
