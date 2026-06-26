package it.diunito.pepper.ui.components.overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientIcons


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeToggle(
    isDark: Boolean,
    onToggle: () -> Unit,
    onResetToSystem: (() -> Unit)? = null
) {
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    Surface(
        shape = RoundedCornerShape(999.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, borderColor),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(999.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .combinedClickable(                  // <<< long click + click
                    onClick = onToggle,
                    onLongClick = { onResetToSystem?.invoke() }
                )
                .padding(horizontal = 8.dp)
        ) {
            val lightSelected = !isDark
            val darkSelected = isDark

            // Toggle structure from light to dark
            // light icon
            Icon(
                painter = ClientIcons.theme_light(),
                contentDescription = "Light theme",
                tint = if (lightSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            // pill
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(width = 32.dp, height = 18.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = if (isDark) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            // dark icon
            Icon(
                painter = ClientIcons.theme_dark(),
                contentDescription = "Dark theme",
                tint = if (darkSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

