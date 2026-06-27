package it.diunito.pepper.ui.screens

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.buttons.AppButton
import it.diunito.pepper.ui.components.buttons.AppButtonColorsRed
import it.diunito.pepper.ui.components.chat.ChatBox
import it.diunito.pepper.ui.components.chat.ChatHeader
import it.diunito.pepper.ui.components.chat.ChatInputBar
import it.diunito.pepper.ui.components.chat.ChatMessage
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.theme.OnlineGreen
import it.diunito.pepper.ui.theme.SendButtonColor
import it.diunito.pepper.ui.theme.UNITOred
import it.diunito.pepper.ui.viewmodel.ChatViewModel
import it.diunito.pepper.ui.scripts.LocalLanguageHandler as lang

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EngageScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    // load chat messages
    val messages: List<ChatMessage> by viewModel.chat.observeAsState(emptyList())
    val isDark = LocalIsDark.current

    var isUserTyping: Boolean by remember { mutableStateOf(false) }
    var isPepperTyping: Boolean by remember { mutableStateOf(false) }

    // load available languages
    val labels = lang.current.labels.collectAsState().value

    // input bar
    var input by rememberSaveable { mutableStateOf("") }
    val inputFocus = remember { FocusRequester() }

    // Pepper status text (dynamic based on state)
    val pepperStatus = when {
        isUserTyping -> labels.pepperListening
        isPepperTyping -> labels.pepperThinking
        else -> labels.pepperReady
    }

    // Status indicator color
    val statusColor = when {
        isUserTyping -> UNITOred
        isPepperTyping -> SendButtonColor
        else -> OnlineGreen
    }

    // Suggestions list
    val suggestions = listOf(
        labels.suggestion1,
        labels.suggestion2,
        labels.suggestion3,
        labels.suggestion4
    )

    // ── Floating animation for Pepper avatar ──
    val infiniteTransition = rememberInfiniteTransition(label = "pepper_float")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        // ══════════════════════════════════════════════
        // LEFT: Chat area (larger — 65% of the screen)
        // ══════════════════════════════════════════════
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .padding(12.dp, 12.dp, 0.dp, 12.dp)
        ) {
            Surface(
                tonalElevation = if (isDark) 0.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(Modifier.fillMaxSize()) {
                    ChatHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    HorizontalDivider(
                        Modifier,
                        DividerDefaults.Thickness,
                        color = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    ChatBox(
                        showUserTyping = isUserTyping,
                        showPepperTyping = isPepperTyping,
                        messages = messages,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .then(
                                if (!isDark) {
                                    Modifier.border(
                                        width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    )
                                } else Modifier
                            )
                    )
                    ChatInputBar(
                        value = input,
                        onValueChange = { input = it },
                        onSend = {
                            val text = input.trim()
                            if (text.isNotEmpty()) {
                                viewModel.stopSpeech()
                                viewModel.dialogueTurn(
                                    updateUserTyping = { isUserTyping = it },
                                    updatePepperTyping = { isPepperTyping = it },
                                    content = text,
                                )
                                input = ""
                            }
                        },
                        onMic = {
                            viewModel.stopSpeech()
                            viewModel.dialogueTurn(
                                updateUserTyping = { isUserTyping = it },
                                updatePepperTyping = { isPepperTyping = it },
                            )
                        },
                        isMicEnabled = !isUserTyping && !isPepperTyping,
                        focusRequester = inputFocus,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Vertical separator
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 32.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(
                alpha = if (isDark) 0.12f else 0.2f
            )
        )

        // ══════════════════════════════════════════════
        // RIGHT: Pepper Status Panel (~35% of the screen)
        // ══════════════════════════════════════════════
        Column(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top: Pepper Avatar + Status ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Spacer to slightly lower the avatar from the top edge
                Spacer(modifier = Modifier.weight(0.5f))

                // Animated Pepper avatar with glow
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = floatingOffset * 6f * density
                        }
                        .size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Glow behind avatar
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .drawBehind {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            statusColor.copy(alpha = 0.15f),
                                            statusColor.copy(alpha = 0.05f),
                                            Color.Transparent
                                        ),
                                        radius = size.maxDimension * 0.65f
                                    )
                                )
                            }
                    )
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = 2.dp,
                                color = statusColor.copy(alpha = 0.4f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pepper),
                            contentDescription = "Pepper",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pepperStatus,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    )
                }

                // Flexible spacer to push the avatar up and suggestions down
                Spacer(modifier = Modifier.weight(1f))

                // ── Suggestion chips ──
                Row(
                    modifier = Modifier.padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💡",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = labels.suggestionsTitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    )
                }

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestions.forEach { suggestion ->
                        SuggestionChip(
                            text = suggestion,
                            isDark = isDark,
                            enabled = !isUserTyping && !isPepperTyping,
                            onClick = {
                                viewModel.stopSpeech()
                                viewModel.dialogueTurn(
                                    updateUserTyping = { isUserTyping = it },
                                    updatePepperTyping = { isPepperTyping = it },
                                    content = suggestion,
                                )
                            }
                        )
                    }
                }
                
                // Spacer below suggestions to lift them towards the middle
                Spacer(modifier = Modifier.weight(1.2f))
            }

            // ── Bottom: "Alla Prossima" button with context ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(bottom = 12.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                // Context text explaining the button
                Text(
                    text = labels.engageWarning,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
                )

                AppButton(
                    label = labels.goodbye,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    onClick = {
                        viewModel.stopSpeech()
                        viewModel.flush(
                            updateUserTyping = { isUserTyping = it },
                            updatePepperTyping = { isPepperTyping = it },
                        )
                    },
                    colors = AppButtonColorsRed(),
                    icon = Icons.Outlined.Refresh
                )
            }
        }
    }
}


// ── Suggestion chip composable ──────────────────────
@Composable
private fun SuggestionChip(
    text: String,
    isDark: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val chipBg = if (isDark) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    }
    val chipBorder = if (isDark) {
        Color.White.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    }
    val chipText = if (isDark) {
        Color.White.copy(alpha = if (enabled) 0.85f else 0.4f)
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = if (enabled) 0.85f else 0.4f)
    }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (enabled) Modifier.clickable { onClick() }
                else Modifier
            ),
        shape = RoundedCornerShape(20.dp),
        color = chipBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, chipBorder)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            color = chipText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}
