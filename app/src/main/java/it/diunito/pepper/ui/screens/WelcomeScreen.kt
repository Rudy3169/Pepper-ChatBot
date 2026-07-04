package it.diunito.pepper.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.buttons.AppButton
import it.diunito.pepper.ui.components.buttons.AppButtonColors
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.theme.ClientIcons
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════
// Color tokens for WelcomeScreen
// ═══════════════════════════════════════════

// Avatar circle
private val AvatarCircleColor = Color(0xFFEFF6FF)

// CTA button
private val ButtonBlue = Color(0xFF3B82F6)
private val ButtonBluePressed = Color(0xFF2563EB)

@Composable
fun WelcomeScreen(
    onStartChat: () -> Unit
) {
    val labels = LocalLanguageHandler.current.labels.collectAsState().value
    val isDark = LocalIsDark.current

    // Title/subtitle colors
    val titleColor = if (isDark) Color(0xFFE9EDEF) else Color(0xFF2F3437)
    val subtitleColor = if (isDark) Color(0xFF8696A0) else Color(0xFF4B5563)

    // ═══════════════════════════════════════════
    // Staggered entrance animations
    // ═══════════════════════════════════════════
    val iconAlpha = remember { Animatable(0f) }
    val iconOffsetY = remember { Animatable(40f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(30f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val subtitleOffsetY = remember { Animatable(25f) }
    val buttonAlpha = remember { Animatable(0f) }
    val buttonOffsetY = remember { Animatable(20f) }

    LaunchedEffect(Unit) {
        // Icon entrance (t = 200ms)
        delay(200L)
        launch { iconAlpha.animateTo(1f, tween(600, easing = EaseOutCubic)) }
        launch { iconOffsetY.animateTo(0f, tween(600, easing = EaseOutCubic)) }

        // Title entrance (t = 350ms)
        delay(150L)
        launch { titleAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { titleOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }

        // Subtitle entrance (t = 520ms)
        delay(170L)
        launch { subtitleAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { subtitleOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }

        // CTA button entrance (t = 670ms)
        delay(150L)
        launch { buttonAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { buttonOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }
    }

    // ═══════════════════════════════════════════
    // Continuous animations
    // ═══════════════════════════════════════════
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")

    // Floating icon – gentle sinusoidal hover
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    // CTA button pulse
    val buttonPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // ═══════════════════════════════════════════
    // Layout — background is handled by AppScaffold
    // ═══════════════════════════════════════════

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Pepper avatar in simple circle ──
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = iconAlpha.value
                    translationY =
                        iconOffsetY.value * density + floatingOffset * 8f * density
                }
                .size(180.dp)
                .clip(CircleShape)
                .background(AvatarCircleColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pepper),
                contentDescription = "Pepper",
                modifier = Modifier.size(130.dp)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // ── Title ────────────────────────────────────────────
        Text(
            text = labels.welcomeTitle,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp,
                letterSpacing = (-1).sp,
                color = titleColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer {
                alpha = titleAlpha.value
                translationY = titleOffsetY.value * density
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ── Subtitle ─────────────────────────────────────────
        Text(
            text = labels.welcomeSubtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = subtitleColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .graphicsLayer {
                    alpha = subtitleAlpha.value
                    translationY = subtitleOffsetY.value * density
                }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ── CTA button ────────────
        Box(
            modifier = Modifier.graphicsLayer {
                alpha = buttonAlpha.value
                translationY = buttonOffsetY.value * density
                scaleX = buttonPulse
                scaleY = buttonPulse
            }
        ) {
            AppButton(
                label = labels.startChatting,
                onClick = onStartChat,
                myIcon = painterResource(R.drawable.ic_chat),
                iconSize = 48.dp,
                fontSize = 24.sp,
                colors = AppButtonColors(
                    fill = ButtonBlue,
                    fillPressed = ButtonBluePressed,
                    border = ButtonBlue.copy(alpha = 0.4f),
                    content = Color.White,
                    glow = ButtonBlue.copy(alpha = 0.3f)
                ),
                width = 320.dp,
                height = 80.dp,
                cornerRadius = 40.dp
            )
        }
    }
}
