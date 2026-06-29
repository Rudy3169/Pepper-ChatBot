package it.diunito.pepper.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientTheme
import it.diunito.pepper.ui.theme.ClientIcons

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    useRed: Boolean = true,
    myIcon: Painter? = null,
    icon: ImageVector? = null,
) {
    val preset = if (useRed) AppButtonColorsRed() else AppButtonColorsYellow()

    AppButton(
        onClick = onClick,
        modifier = modifier,
        colors = preset,
        cornerRadius = 12.dp,
        myIcon = myIcon,
        icon = icon
    )
}

@Preview(showBackground = true, name = "AppIconButton • variants")
@Composable
private fun Preview_AppIconButton() {
    ClientTheme {
        Column {
            AppIconButton(onClick = {}, icon = Icons.Outlined.Close)
            Spacer(modifier = Modifier.height(8.dp))
            AppIconButton(onClick = {}, myIcon = ClientIcons.pause(), useRed = false)
        }
    }
}
