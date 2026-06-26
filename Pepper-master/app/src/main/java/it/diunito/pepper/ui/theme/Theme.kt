package it.diunito.pepper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    // Brand roles
    primary = AIRblue,
    onPrimary = white,
    primaryContainer = AIRblue25,
    onPrimaryContainer = UNITOgrey,

    secondary = AIRyellow,
    onSecondary = black,
    secondaryContainer = AIRyellow25,
    onSecondaryContainer = UNITOgrey,

    tertiary = UNITOred,
    onTertiary = white,
    tertiaryContainer = granata,
    onTertiaryContainer = white,

    // Neutrals & Surfaces
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val DarkColors = darkColorScheme(
    primary = AIRblue50,
    onPrimary = black,
    primaryContainer = AIRblue,
    onPrimaryContainer = white,

    secondary = AIRyellow50,
    onSecondary = black,
    secondaryContainer = AIRyellow,
    onSecondaryContainer = black,

    tertiary = UNITOred,
    onTertiary = white,
    tertiaryContainer = granata,
    onTertiaryContainer = white,

    // Neutrals & Surfaces
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

@Composable
fun ClientTheme(
    forceDark: Boolean? = null, // follow device's settings
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // blocca Material You: niente deviazioni dal brand
    content: @Composable () -> Unit
) {
    val darkTheme = forceDark ?: darkTheme
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,  // uses custom type.kt
        content = content
    )
}
