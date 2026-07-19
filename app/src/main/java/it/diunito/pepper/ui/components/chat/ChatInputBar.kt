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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Arrangement
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
    isMicEnabled: Boolean = true,
    isProcessing: Boolean = false,
    onStop: (() -> Unit)? = null
) {
    // recall language labels from LanguageHandler
    val labels = lang.current.labels.collectAsState().value
    val isDark = LocalIsDark.current

    // Adaptive colors
    val barBackground = if (isDark) HeaderDark else MaterialTheme.colorScheme.surface
    val fieldBackground = if (isDark) InputDarkField else white
    val textColor = if (isDark) white.copy(alpha = 0.92f) else MaterialTheme.colorScheme.onBackground
    val placeholderColor = if (isDark) white.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)

    val hasText = value.isNotBlank()

    Surface(
        tonalElevation = 0.dp,
        color = Color.Transparent
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier.widthIn(max = 860.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var tfModifier = Modifier.weight(1f)
                if (focusRequester != null) tfModifier = tfModifier.focusRequester(focusRequester)

                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = tfModifier,
                    enabled = !isProcessing,
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
                        disabledContainerColor = fieldBackground.copy(alpha = 0.6f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(Modifier.width(8.dp))

                // Three states: stop (processing), send (has text), mic (no text)
                AnimatedContent(
                    targetState = when {
                        isProcessing -> "stop"
                        hasText -> "send"
                        else -> "mic"
                    },
                    transitionSpec = {
                        (fadeIn() + scaleIn(initialScale = 0.8f))
                            .togetherWith(fadeOut() + scaleOut(targetScale = 0.8f))
                    },
                    label = "mic_send_stop_swap",
                    modifier = Modifier.padding(bottom = 4.dp)
                ) { state ->
                    Surface(
                        shape = CircleShape,
                        color = when (state) {
                            "stop" -> Color(0xFFE53935) // Red for stop
                            "send" -> Color(0xFF008069)
                            else -> if (isMicEnabled) Color(0xFF008069) else Color(0xFF008069).copy(alpha = 0.3f)
                        },
                        shadowElevation = when {
                            isDark -> 0.dp
                            state == "mic" && !isMicEnabled -> 0.dp
                            else -> 1.dp
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(
                                enabled = when (state) {
                                    "stop" -> true
                                    "send" -> true
                                    else -> isMicEnabled
                                },
                                onClick = {
                                    when (state) {
                                        "stop" -> onStop?.invoke()
                                        "send" -> onSend()
                                        else -> onMic?.invoke()
                                    }
                                }
                            )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            when (state) {
                                "stop" -> {
                                    // Stop button (filled square)
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(white)
                                    )
                                }
                                "send" -> {
                                    // Send button
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = white,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                else -> {
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
    }
}
