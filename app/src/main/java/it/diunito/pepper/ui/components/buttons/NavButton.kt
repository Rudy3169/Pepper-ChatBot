package it.diunito.pepper.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import it.diunito.pepper.ui.theme.ClientTheme
import androidx.compose.material.icons.automirrored.outlined.ArrowForward

@Composable
fun NavButton(
    label: String,
    onClick: () -> Unit,
    useBlue: Boolean = true,
    myIcon: Painter? = null,
    icon: ImageVector? = null,
) {
    val preset = if (useBlue) AppButtonColorsBlue() else AppButtonColorsYellow()

    AppButton(
        label = label,
        onClick = onClick,
        width  = ButtonFixedWidth,
        height = ButtonFixedHeight,
        colors = preset,
        myIcon = myIcon,
        icon = icon
    )
}

@Preview(showBackground = true, name = "NavButton • Blue")
@Composable
private fun Preview_NavButton_Blue() {
    ClientTheme {
        NavButton(label = "Go", onClick = {}, icon = Icons.AutoMirrored.Outlined.ArrowForward)
    }
}

