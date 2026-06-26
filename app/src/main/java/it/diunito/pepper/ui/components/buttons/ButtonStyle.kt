package it.diunito.pepper.ui.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientTheme

// shared size so every nav button is identical
val ButtonFixedWidth: Dp = 260.dp
val ButtonFixedHeight: Dp = 60.dp
val ButtonIconSize: Dp = 24.dp
val ButtonIconSpacing: Dp = 8.dp
val ButtonMinHeight: Dp = 48.dp
val ButtonMinWidth: Dp = 48.dp


// color palette
data class AppButtonColors(
    val light: Color,   // highlight
    val mid: Color,     // fill
    val dark: Color,    // bottom border
    val content: Color  // text + icon
)

@Composable
fun AppButtonColorsBlue() = AppButtonColors(
    light = MaterialTheme.colorScheme.primaryContainer,
    mid   = MaterialTheme.colorScheme.primary,
    dark  = MaterialTheme.colorScheme.primary.copy(alpha = .85f),
    content = MaterialTheme.colorScheme.onPrimary
)

@Composable
fun AppButtonColorsYellow() = AppButtonColors(
    light = MaterialTheme.colorScheme.secondaryContainer,
    mid   = MaterialTheme.colorScheme.secondary,
    dark  = MaterialTheme.colorScheme.secondary.copy(alpha = .85f),
    content = MaterialTheme.colorScheme.onSecondary
)

@Composable
fun AppButtonColorsGray() = AppButtonColors(
    light = MaterialTheme.colorScheme.surface,
    mid   = MaterialTheme.colorScheme.surfaceVariant,
    dark  = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .5f),
    content = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
fun AppButtonColorsRed() = AppButtonColors(
    light = MaterialTheme.colorScheme.errorContainer,
    mid   = MaterialTheme.colorScheme.error,
    dark  = MaterialTheme.colorScheme.error.copy(alpha = .85f),
    content = MaterialTheme.colorScheme.onError
)


/** PREVIEWS **/

@Preview(showBackground = true, name = "NavButton • Blue")
@Composable
private fun Preview_NavButton_Blue() {
    ClientTheme {
        NavButton(label = "Go", onClick = {})
    }
}

@Preview(showBackground = true, name = "AppButton • Yellow")
@Composable
private fun Preview_AppButton_Yellow() {
    ClientTheme {
        AppButton(label = "Challenge", onClick = {}, colors = AppButtonColorsYellow())
    }
}

@Preview(showBackground = true, name = "AppButton • Gray")
@Composable
private fun Preview_AppButton_Gray() {
    ClientTheme {
        AppButton(label = "Challenge", onClick = {}, colors = AppButtonColorsGray())
    }
}

@Preview(showBackground = true, name = "AppButton • Red")
@Composable
private fun Preview_AppButton_Red() {
    ClientTheme {
        AppButton(label = "Challenge", onClick = {}, colors = AppButtonColorsRed())
    }
}
