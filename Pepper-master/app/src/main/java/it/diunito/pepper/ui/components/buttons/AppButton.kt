package it.diunito.pepper.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.theme.ClientTheme

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: () -> Unit,
    colors: AppButtonColors? = null,
    width: Dp? = null,
    height: Dp? = null,
    cornerRadius: Dp = 999.dp,
    myIcon: Painter? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(cornerRadius)
    val fixedSize: Boolean = (width != null) && (height != null)
    val iconOnly: Boolean = (myIcon != null || icon != null) && label.isNullOrEmpty()

    var validatedColors: AppButtonColors

    if (colors == null || !enabled){
        validatedColors = if (enabled) AppButtonColorsYellow() else AppButtonColorsGray()
    } else {
        validatedColors = colors
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = if (fixedSize) {
            modifier.width(width).height(height)
        } else {
            modifier
                .widthIn(min = ButtonMinWidth)
                .heightIn(min = ButtonMinHeight)
        },
        shape = shape,
        contentPadding = if(iconOnly) PaddingValues(0.dp) else ButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor   = validatedColors.content,
            disabledContainerColor = Color.Transparent,
            disabledContentColor   = validatedColors.content.copy(alpha = .6f)
        ),
        elevation = null
    ) {
        Box(
            modifier = Modifier
                .then(if (fixedSize) Modifier.fillMaxSize() else Modifier.padding(PaddingValues(6.dp, 3.dp)))
                .clip(shape)
                .drawBehind {
                    // bail out if we don't have space
                    if (size.width<=1f || size.height<=1f) return@drawBehind

                    // dark bottom border
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.6f to Color.Transparent,
                            1f to validatedColors.dark.copy(alpha = .22f)
                        ),
                        cornerRadius = CornerRadius(size.height, size.height)
                    )
                    // glossy fill
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            0.00f to validatedColors.light,
                            0.35f to validatedColors.mid,
                            1.00f to validatedColors.mid.copy(alpha = .90f)
                        ),
                        cornerRadius = CornerRadius(size.height, size.height)
                    )
                    // inner highlight
                    val stroke = 3.dp.toPx()
                    inset(stroke, stroke, stroke, stroke) {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                0f to Color.White.copy(alpha = .35f),
                                1f to Color.Transparent
                            ),
                            cornerRadius = CornerRadius(size.height, size.height)
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = if(iconOnly) modifier.padding(18.dp) else modifier.padding(18.dp,6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                when{
                    myIcon != null -> Icon(
                        painter = myIcon,
                        contentDescription = label,
                        modifier = Modifier.size(ButtonIconSize)
                    )
                    icon != null -> Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(ButtonIconSize)
                    )
                }
                if (!label.isNullOrEmpty()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "NavButton • Blue")
@Composable
private fun Preview_NavButton_Blue() {
    ClientTheme {
        NavButton(label = "Go", onClick = {})
    }
}

@Preview(showBackground = true, name = "AppButton • enabled")
@Composable
private fun Preview_AppButton_Yellow() {
    ClientTheme {
        AppButton(label = "Label", onClick = {}, icon = Icons.Outlined.Done, enabled = true)
    }
}

@Preview(showBackground = true, name = "AppButton • disabled")
@Composable
private fun Preview_AppButton_Gray() {
    ClientTheme {
        AppButton(label = "Label", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "AppButton • Red")
@Composable
private fun Preview_AppButton_Red() {
    ClientTheme {
        AppButton(label= "", onClick = {},icon = Icons.Outlined.Close, colors = AppButtonColorsRed())
    }
}

@Preview(showBackground = true, name = "IconButton • Red")
@Composable
private fun Preview_AppButton_IconOnly() {
    ClientTheme{
        AppIconButton(onClick = {}, myIcon = ClientIcons.pause())
    }
}

