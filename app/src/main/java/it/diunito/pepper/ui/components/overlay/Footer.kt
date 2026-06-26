package it.diunito.pepper.ui.components.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientPictures

@Composable
fun FooterLogos(
    isDark: Boolean,
    modifier: Modifier = Modifier,
    onAirlabClick: (() -> Unit)? = null,
    onDipInfoClick: (() -> Unit)? = null,
    onUnitoClick:   (() -> Unit)? = null,
    height: Dp = 60.dp
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Logo(ClientPictures.dipinfo(isDark), "Logo DipInfo", onDipInfoClick)
                    Logo(ClientPictures.airlab(isDark), "Logo AIRLab", onAirlabClick)
                    Logo(ClientPictures.unito(isDark), "Logo UniTo", onUnitoClick)
                }
            }
        }
    }
}

@Composable
private fun Logo(
    res: Painter,
    desc: String,
    onClick: (() -> Unit)?
) {
    val m = Modifier
        .fillMaxHeight(0.95f)
        .semantics { contentDescription = desc }

    Image(
        painter = res,
        contentDescription = desc,
        contentScale = ContentScale.Fit,
        modifier = if (onClick != null) m.clickable { onClick() } else m
    )
}

