package it.diunito.pepper.ui.components.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientIcons

@Composable
fun LanguageSwitch(
    language: String,
    onSwitchLanguage: () -> Unit,
) {
    val icon: Int = ClientIcons.lang_flag(language)

    Image(
        painter = painterResource(id = icon),
        contentDescription = "Switch language",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable { onSwitchLanguage() }
    )
}

@Preview
@Composable
fun LanguageSwitchPreview() {
    LanguageSwitch(language = "it", onSwitchLanguage = {})
}
