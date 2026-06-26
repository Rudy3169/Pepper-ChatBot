package it.diunito.pepper.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.GoogleFont

/** GOOGLE FONTS **/

// recall API to use Google Fonts
private val provider = GoogleFont.Provider (
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = it.diunito.pepper.R.array.com_google_android_gms_fonts_certs
)

// --- AIRLAB WEBSITE FONTS --- //

// associate fonts
private val WorkSansFont = GoogleFont("Work Sans")
private val RobotoFont = GoogleFont("Roboto")

// define styles and weights
private val WorkSans = FontFamily(
    Font(googleFont = WorkSansFont, fontProvider = provider, weight = FontWeight.Normal)  // 400
)
private val Roboto = FontFamily(
    Font(googleFont = RobotoFont, fontProvider = provider, weight = FontWeight.Light),    // 300
    Font(googleFont = RobotoFont, fontProvider = provider, weight = FontWeight.Normal),   // 400
    Font(googleFont = RobotoFont, fontProvider = provider, weight = FontWeight.SemiBold)  // 600
)

// Set of Material typography on AIRlab's style
val Typography = Typography(
    // Headers (display/headline/title) → Work Sans 400
    displayLarge  = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall  = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp),

    headlineLarge = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium= TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 24.sp, lineHeight = 32.sp),

    titleLarge    = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium   = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    titleSmall    = TextStyle(fontFamily = WorkSans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),

    // Text → Roboto (400 primary, 300 descriptions)
    bodyLarge     = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.Normal,  fontSize = 16.sp, lineHeight = 24.sp), // testo principale
    bodyMedium    = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.Light,   fontSize = 14.sp, lineHeight = 20.sp), // descrizioni
    bodySmall     = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.Light,   fontSize = 12.sp, lineHeight = 16.sp),

    // Labels (buttons, chip, badge) → Roboto 600
    labelLarge    = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium   = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall    = TextStyle(fontFamily = Roboto, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 16.sp)
)

// links
object AppTextStyles {
    val Link = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}