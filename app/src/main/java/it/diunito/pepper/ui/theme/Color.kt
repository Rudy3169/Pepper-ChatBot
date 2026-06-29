package it.diunito.pepper.ui.theme

import androidx.compose.ui.graphics.Color

val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)

// AIRlab colors
val almostBlack = Color (0xFF0D0D0D)
val UNITOgrey = Color(0xFF54565A)
val lightGrey = Color(0xFF949494)
val UNITOred = Color(0xFFEA0029)
val granata = Color(0xFF73142D)
val AIRyellow = Color(0xFFFFB233)
val AIRyellow50 = Color(0xFFFFCB76)
val AIRyellow25 = Color(0xFFFFE6BC)

// links and buttons
val AIRblue = Color(0xFF1881AB)
val AIRblue50 = Color(0xFF5EB8DC)
val AIRblue25 = Color(0xFFB3E1F1)
val Purple = Color(0xFF6650a4)
val Purple50 = Color(0xFFAA6FF7)
val Purple25 = Color(0xFFD3B9F6)


// === Neutral & Surfaces ===
// Light theme
val BackgroundLight = white
val SurfaceLight = AIRyellow25
val OnBackgroundLight = UNITOgrey
val OnSurfaceLight = UNITOgrey
val SurfaceVariantLight = lightGrey
val OnSurfaceVariantLight = black
val OutlineLight = lightGrey

// Dark theme
val BackgroundDark = almostBlack
val SurfaceDark = Color(0xFF1A1A1A)
val OnBackgroundDark = lightGrey
val OnSurfaceDark = lightGrey
val SurfaceVariantDark = UNITOgrey
val OnSurfaceVariantDark = white
val OutlineDark = UNITOgrey

// === Dark Mode Chat Colors (AIRlab-branded conversational UI) ===
val BubbleDarkIncoming = Color(0xFF242424) // Dark Grey for Pepper
val BubbleDarkOutgoing = AIRblue // Brand Blue for User
val OnBubbleDarkIncoming = Color(0xFFE9EDEF)
val OnBubbleDarkOutgoing = white
val ChatDarkSurface = Color(0xFF121212)
val InputDarkField = Color(0xFF242424)
val HeaderDark = Color(0xFF1A1A1A)
val OnlineGreen = Color(0xFF25D366)

// Light mode chat-specific
val BubbleLightIncoming = white // White for Pepper
val BubbleLightOutgoing = AIRblue // Brand Blue for User
val OnBubbleLightIncoming = Color(0xFF1A1A1A)
val OnBubbleLightOutgoing = white
