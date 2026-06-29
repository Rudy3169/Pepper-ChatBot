package it.diunito.pepper.ui.components.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.theme.HeaderDark
import it.diunito.pepper.ui.theme.InputDarkField

import it.diunito.pepper.ui.theme.white
import it.diunito.pepper.ui.scripts.LocalLanguageHandler as lang


@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onMic: (() -> Unit)? = null,
    isMicEnabled: Boolean = true
) {
    // recall language labels from LanguageHandler
    val labels = lang.current.labels.collectAsState().value
    val isDark = LocalIsDark.current

    // Adaptive colors
    val barBackground = if (isDark) HeaderDark else MaterialTheme.colorScheme.surface
    val fieldBackground = if (isDark) InputDarkField else MaterialTheme.colorScheme.background
    val textColor = if (isDark) white.copy(alpha = 0.92f) else MaterialTheme.colorScheme.onBackground
    val placeholderColor = if (isDark) white.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)

    val hasText = value.isNotBlank()

    Surface(
        tonalElevation = if (isDark) 0.dp else 1.dp,
        color = barBackground
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var tfModifier = Modifier.weight(1f)
            if (focusRequester != null) tfModifier = tfModifier.focusRequester(focusRequester)

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = tfModifier,
                placeholder = {
                    Text(
                        text = labels.chatInputPlaceholder,
                        color = placeholderColor
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBackground,
                    unfocusedContainerColor = fieldBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(Modifier.width(8.dp))

            // Conversational UI logic: microphone icon when input is empty, send icon when text is present
            AnimatedContent(
                targetState = hasText,
                transitionSpec = {
                    (fadeIn() + scaleIn(initialScale = 0.8f))
                        .togetherWith(fadeOut() + scaleOut(targetScale = 0.8f))
                },
                label = "mic_send_swap",
                modifier = Modifier.padding(bottom = 4.dp)
            ) { showSend ->
                Surface(
                    shape = CircleShape,
                    color = when {
                        showSend -> MaterialTheme.colorScheme.primary
                        !isMicEnabled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        else -> MaterialTheme.colorScheme.primary
                    },
                    shadowElevation = if (isDark || (!showSend && !isMicEnabled)) 0.dp else 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable(
                            enabled = if (showSend) true else isMicEnabled,
                            onClick = { if (showSend) onSend() else onMic?.invoke() }
                        )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (showSend) {
                            // Send button
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = white,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            // Mic button
                            Icon(
                                painter = ClientIcons.mic(),
                                contentDescription = labels.talk,
                                tint = if (isMicEnabled) white else white.copy(alpha = 0.5f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
