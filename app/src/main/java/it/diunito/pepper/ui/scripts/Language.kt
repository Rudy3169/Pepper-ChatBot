package it.diunito.pepper.ui.scripts

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.utils.io.charsets.Charset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TextLabel(
    val chatTitle: String,
    val chatMessage: String,
    val chatInputPlaceholder: String,
    val engageTitle: String,
    val engageInstruction: String,
    val engageWarning: String,
    val talk: String,
    val goodbye: String,
    val start: String,
    val repeat: String,
    val next: String,
    val emergency: String,
    val timerTitle: String,
    val welcomeTitle: String,
    val welcomeSubtitle: String,
    val startChatting: String,
    val pepperReady: String,
    val pepperListening: String,
    val pepperThinking: String,
    val suggestion1: String,
    val suggestion2: String,
    val suggestion3: String,
    val suggestion4: String,
    val suggestionsTitle: String
)

@Serializable
data class Language(
    val it: TextLabel,
    val en: TextLabel
)

enum class AppLanguage(val code: String) {
    IT("it"),
    EN("en")
}


fun loadLanguages(context: Context): Language {
    val jsonString = context.assets
        .open("translations.json")
        .bufferedReader(Charset.defaultCharset())
        .use { it.readText() }

    return Json.decodeFromString(Language.serializer(), jsonString)
}

class LanguageHandler(
    private val all: Language,
    initial: AppLanguage = AppLanguage.IT
) {
    private val _currentLanguage = MutableStateFlow(initial)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage

    private val _labels = MutableStateFlow(selectLabels(initial))
    val labels: StateFlow<TextLabel> = _labels

    private fun selectLabels(lang: AppLanguage): TextLabel {
        return when (lang) {
            AppLanguage.IT -> all.it
            AppLanguage.EN -> all.en
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        _labels.value = selectLabels(lang)
    }
}

val LocalLanguageHandler = staticCompositionLocalOf<LanguageHandler> {
    error("No LanguageManager provided")
}