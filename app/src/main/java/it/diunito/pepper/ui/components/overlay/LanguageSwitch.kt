package it.diunito.pepper.ui.components.overlay

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import it.diunito.pepper.ui.theme.ClientIcons

@Composable
fun LanguageSwitch(
    language: String,
    onSwitchLanguage : () -> Unit,
) {
    val icon: Int = ClientIcons.lang_flag(language)

    IconButton(onClick = onSwitchLanguage) {
        androidx.compose.material3.Icon(
            painter = androidx.compose.ui.res.painterResource(id = icon),
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.Unspecified
        )
    }
}

@Preview
@Composable
fun LanguageSwitchPreview() {
    LanguageSwitch(language = "it", onSwitchLanguage = {})
}

