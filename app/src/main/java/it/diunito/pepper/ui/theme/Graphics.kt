package it.diunito.pepper.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import it.diunito.pepper.R

object ClientIcons {
    // languages (Figma circular flags)
    @Composable
    fun lang_flag(lang: String) = if (lang == "it") {
        R.drawable.ic_flag_eng
    } else {
        R.drawable.ic_flag_ita
    }

    // theme switch (Figma moon/sun icons)
    @Composable
    fun theme_dark() = painterResource(R.drawable.ic_dark_mode)

    @Composable
    fun theme_light() = painterResource(R.drawable.ic_light_mode)

    // navigation
    @Composable
    fun conversation() = painterResource(R.drawable.ic_conversation)

    @Composable
    fun cook() = painterResource(R.drawable.ic_cook)


    // custom theme icons (Figma microphone)
    @Composable
    fun mic() = painterResource(R.drawable.ic_mic_black)

    @Composable
    fun micWhite() = painterResource(R.drawable.ic_mic_white)

    @Composable
    fun send() = painterResource(R.drawable.ic_send)

    @Composable
    fun pause() = painterResource(R.drawable.ic_pause)

}

object ClientPictures {
    @Composable
    fun airlab(isDark:Boolean) = if (isDark) painterResource(R.drawable.airlab_dark) else painterResource(R.drawable.airlab_light)

    @Composable
    fun dipinfo(isDark:Boolean) = if (isDark) painterResource(R.drawable.dipinfo_dark) else painterResource(R.drawable.dipinfo_light)

    @Composable
    fun unito(isDark:Boolean) = if (isDark) painterResource(R.drawable.unito_dark) else painterResource(R.drawable.unito_light)

    @Composable
    fun wallpaperDark() = painterResource(R.drawable.ic_pepper) // placeholder — wallpaper removed

    @Composable
    fun wallpaperLight() = painterResource(R.drawable.ic_pepper) // placeholder — wallpaper removed
}