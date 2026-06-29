package it.diunito.pepper.ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
    cornerRadius: Dp = 16.dp,
    myIcon: Painter? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(cornerRadius)
    val fixedSize = width != null && height != null
    val iconOnly = (myIcon != null || icon != null) && label.isNullOrEmpty()

    val resolvedColors: AppButtonColors = if (colors == null || !enabled) {
        if (enabled) AppButtonColorsYellow() else AppButtonColorsGray()
    } else {
        colors
    }

    // ── Interaction-driven animations ──
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "scale"
    )
    val animatedFill by animateColorAsState(
        targetValue = if (isPressed && enabled) resolvedColors.fillPressed else resolvedColors.fill,
        animationSpec = tween(durationMillis = 150),
        label = "fill"
    )
    val disabledAlpha = if (enabled) 1f else 0.50f

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = (if (width != null && height != null) {
            modifier.width(width).height(height)
        } else {
            modifier
                .widthIn(min = ButtonMinWidth)
                .heightIn(min = ButtonMinHeight)
        })
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = shape,
                ambientColor = resolvedColors.glow,
                spotColor = resolvedColors.glow
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = disabledAlpha
            }
            .drawBehind {
                if (size.width <= 1f || size.height <= 1f) return@drawBehind

                val cr = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())

                // Layer 1 — Solid fill
                drawRoundRect(
                    color = animatedFill,
                    cornerRadius = cr
                )

                // Layer 2 — Top-edge highlight (subtle glass reflection)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        0.0f to Color.White.copy(alpha = .18f),
                        0.4f to Color.White.copy(alpha = .04f),
                        1.0f to Color.Transparent
                    ),
                    cornerRadius = cr,
                    size = Size(size.width, size.height * 0.5f)
                )

                // Layer 3 — Bottom edge shadow line
                val shadowHeight = 2.dp.toPx()
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black.copy(alpha = .12f),
                        startY = size.height - shadowHeight,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, size.height - shadowHeight),
                    size = Size(size.width, shadowHeight),
                    cornerRadius = cr
                )
            },
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = resolvedColors.content,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = resolvedColors.content
        ),
        elevation = null
    ) {
        Row(
            modifier = if (iconOnly) Modifier.padding(14.dp)
            else Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
                when {
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
                    if (myIcon != null || icon != null) {
                        Spacer(modifier = Modifier.width(ButtonIconSpacing))
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
    }
}

// ════════════════════════════════════════════════
// Previews
// ════════════════════════════════════════════════

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
        AppButton(label = "", onClick = {}, icon = Icons.Outlined.Close, colors = AppButtonColorsRed())
    }
}

@Preview(showBackground = true, name = "IconButton • Pause")
@Composable
private fun Preview_AppButton_IconOnly() {
    ClientTheme {
        AppIconButton(onClick = {}, myIcon = ClientIcons.pause())
    }
}
