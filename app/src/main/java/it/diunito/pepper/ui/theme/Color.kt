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
val BubbleDarkIncoming = Color(0xFF2B323D) // Pepper in dark mode
val BubbleDarkOutgoing = Color(0xFFB90016) // User in dark mode
val OnBubbleDarkIncoming = Color(0xFFE9EDEF)
val OnBubbleDarkOutgoing = Color(0xFFE9EDEF)
val TimeDarkIncoming = Color(0xFF8696A0) // Pepper time text
val TimeDarkOutgoing = Color(0xFFFFB3B3) // User time text

val ChatDarkSurface = Color(0xFF141416) // Chat background
val InputDarkField = Color(0xFF2B323D) // Input bar background
val HeaderDark = Color(0xFF1E1F22) // Header/footer/LLM switcher
val SendButtonDark = Color(0xFFD6001C)

val OnlineGreen = Color(0xFF25D366)

// Light mode chat-specific
val BubbleLightIncoming = Color(0xFFFFFFFF) // Pepper in light mode
val BubbleLightOutgoing = Color(0xFFD6001C) // User in light mode
val OnBubbleLightIncoming = Color(0xFF000000)
val OnBubbleLightOutgoing = Color(0xFFFFFFFF)
val TimeLightIncoming = Color(0xFF8696A0) // Pepper time text
val TimeLightOutgoing = Color(0xFFFFB3B3) // User time text
val ChatLightBackground = Color(0xFFE5E5EA)
val SendButtonLight = Color(0xFFD6001C)
