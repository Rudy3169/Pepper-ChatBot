package it.diunito.pepper.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import it.diunito.pepper.ui.theme.ClientTheme
import it.diunito.pepper.ui.theme.ClientIcons

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    useRed: Boolean = true,
    myIcon: Painter? = null,
    icon: ImageVector? = null,
) {
    val preset = if (useRed) AppButtonColorsRed() else AppButtonColorsYellow()

    AppButton(
        onClick = onClick,
        colors = preset,
        myIcon = myIcon,
        icon = icon
    )
}

@Preview(showBackground = true, name = "NavButton • Blue")
@Composable
private fun Preview_AppIconButton() {
    ClientTheme {
        Column() {
            AppIconButton(onClick = {}, icon = Icons.Outlined.Close)
            AppIconButton(onClick = {}, myIcon = ClientIcons.pause(), useRed = false)
        }
    }
}


