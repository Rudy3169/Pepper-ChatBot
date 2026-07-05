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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
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
import it.diunito.pepper.ui.theme.AIRblue
import it.diunito.pepper.ui.theme.OnlineGreen
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
        isPepperTyping -> AIRblue
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
        // Chat area (100% of the screen)
        // ══════════════════════════════════════════════
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image (Tiled 85%)
            val context = LocalContext.current
            val bgResId = if (isDark) R.drawable.chatbot_chat_dark else R.drawable.chatbot_chat_light
            val bgBrush = androidx.compose.runtime.remember(bgResId) {
                val original = BitmapFactory.decodeResource(context.resources, bgResId)
                val scaled = Bitmap.createScaledBitmap(
                    original,
                    (original.width * 0.85).toInt(),
                    (original.height * 0.85).toInt(),
                    true
                )
                ShaderBrush(
                    ImageShader(
                        scaled.asImageBitmap(),
                        TileMode.Repeated,
                        TileMode.Repeated
                    )
                )
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgBrush)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Column(Modifier.fillMaxSize()) {
                    // Header is now in AppScaffold
                    ChatBox(
                        showUserTyping = isUserTyping,
                        showPepperTyping = isPepperTyping,
                        messages = messages,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()

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
        AIRblue.copy(alpha = 0.15f)
    } else {
        AIRblue.copy(alpha = 0.10f)
    }
    val chipBorder = if (isDark) {
        AIRblue.copy(alpha = 0.3f)
    } else {
        AIRblue.copy(alpha = 0.3f)
    }
    val chipText = if (isDark) {
        AIRblue.copy(alpha = if (enabled) 1f else 0.4f)
    } else {
        AIRblue.copy(alpha = if (enabled) 1f else 0.4f)
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
