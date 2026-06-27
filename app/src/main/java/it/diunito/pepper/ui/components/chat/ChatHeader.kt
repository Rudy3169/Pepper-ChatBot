package it.diunito.pepper.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.theme.HeaderDark
import it.diunito.pepper.ui.theme.OnlineGreen
import it.diunito.pepper.ui.theme.white

@Composable
fun ChatHeader(
    modifier: Modifier = Modifier,
) {
    // recall language labels from LanguageHandler
    val labels = LocalLanguageHandler.current.labels.collectAsState().value
    val isDark = LocalIsDark.current

    val title = labels.chatTitle
    val subtitle = labels.chatMessage

    // Adaptive header colors
    val headerBg = if (isDark) HeaderDark else MaterialTheme.colorScheme.primary
    val headerContent = if (isDark) white.copy(alpha = 0.95f) else MaterialTheme.colorScheme.onPrimary
    val headerSubContent = if (isDark) white.copy(alpha = 0.65f) else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)

    Surface(
        tonalElevation = if (isDark) 0.dp else 2.dp,
        color = headerBg,
        shadowElevation = if (isDark) 0.dp else 2.dp
    ) {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- Chat icon with online indicator ---
                Box {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.background
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_conversation),
                            contentDescription = "Chat icon",
                            tint = if (isDark) white.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    // Online indicator dot
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .clip(CircleShape)
                            .background(headerBg) // outer ring matching header
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(OnlineGreen)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // --- Title and subtitle ---
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = headerContent
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = headerSubContent
                    )
                }
            }
        }
    }
}
