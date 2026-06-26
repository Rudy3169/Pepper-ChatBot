package it.diunito.pepper.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import it.diunito.pepper.R

object ClientIcons {
    // languages
    @Composable
    fun lang_flag(lang: String) = if (lang == "it") {
        R.drawable.flag_eng
    } else {
        R.drawable.flag_ita
    }

    // theme switch
    @Composable
    fun theme_dark() = painterResource(R.drawable.ic_theme_dark)

    @Composable
    fun theme_light() = painterResource(R.drawable.ic_theme_light)

    // navigation
    @Composable
    fun conversation() = painterResource(R.drawable.ic_conversation)

    @Composable
    fun cook() = painterResource(R.drawable.ic_cook)


    // custom theme icons
    @Composable
    fun mic() = painterResource(R.drawable.ic_mic)

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
    fun background(isDark:Boolean) = if (isDark) R.drawable.chatbot_chat_dark else R.drawable.chatbot_chat_light
}