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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.buttons.AppButton
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.theme.AIRblue
import it.diunito.pepper.ui.theme.AIRblue25
import it.diunito.pepper.ui.theme.AIRyellow
import it.diunito.pepper.ui.theme.AIRyellow25
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.theme.UNITOred
import it.diunito.pepper.ui.theme.granata
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onStartChat: () -> Unit
) {
    val labels = LocalLanguageHandler.current.labels.collectAsState().value

    // Extract theme colors for use inside drawBehind (non-composable scope)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    // ═══════════════════════════════════════════
    // Staggered entrance animations
    // ═══════════════════════════════════════════
    val iconAlpha = remember { Animatable(0f) }
    val iconOffsetY = remember { Animatable(40f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(30f) }
    val dividerAlpha = remember { Animatable(0f) }
    val dividerScale = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val subtitleOffsetY = remember { Animatable(25f) }
    val buttonAlpha = remember { Animatable(0f) }
    val buttonOffsetY = remember { Animatable(20f) }
    val footerAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Icon entrance (t = 200ms)
        delay(200L)
        launch { iconAlpha.animateTo(1f, tween(600, easing = EaseOutCubic)) }
        launch { iconOffsetY.animateTo(0f, tween(600, easing = EaseOutCubic)) }

        // Title entrance (t = 350ms)
        delay(150L)
        launch { titleAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { titleOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }

        // Decorative divider entrance (t = 450ms)
        delay(100L)
        launch { dividerAlpha.animateTo(1f, tween(400, easing = EaseOutCubic)) }
        launch { dividerScale.animateTo(1f, tween(600, easing = EaseOutCubic)) }

        // Subtitle entrance (t = 570ms)
        delay(120L)
        launch { subtitleAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { subtitleOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }

        // CTA button entrance (t = 720ms)
        delay(150L)
        launch { buttonAlpha.animateTo(1f, tween(500, easing = EaseOutCubic)) }
        launch { buttonOffsetY.animateTo(0f, tween(500, easing = EaseOutCubic)) }

        // Footer entrance (t = 920ms)
        delay(200L)
        footerAlpha.animateTo(1f, tween(600, easing = EaseOutCubic))
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

    // Background blob breathing animations (different periods for organic feel)
    val blobScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )
    val blobScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )
    val blobScale3 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob3"
    )

    // ═══════════════════════════════════════════
    // Layout
    // ═══════════════════════════════════════════

    // Rich multi-stop background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            backgroundColor,
            surfaceVariantColor.copy(alpha = 0.15f),
            backgroundColor
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // ── Decorative background blobs ──────────────────────

        // Top-end blob (UNITOred tint)
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-60).dp)
                .scale(blobScale1)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            UNITOred.copy(alpha = 0.10f),
                            UNITOred.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Bottom-start blob (AIRblue tint)
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 80.dp)
                .scale(blobScale2)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AIRblue25.copy(alpha = 0.14f),
                            AIRblue25.copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center-start blob (AIRyellow tint)
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-40).dp, y = (-80).dp)
                .scale(blobScale3)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AIRyellow25.copy(alpha = 0.10f),
                            AIRyellow25.copy(alpha = 0.02f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // ── Main content column ──────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Pepper icon with gradient glow ───────────────
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = iconAlpha.value
                        translationY =
                            iconOffsetY.value * density + floatingOffset * 8f * density
                    }
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow halo
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        UNITOred.copy(alpha = 0.12f),
                                        AIRblue25.copy(alpha = 0.06f),
                                        Color.Transparent
                                    ),
                                    radius = size.maxDimension * 0.7f
                                )
                            )
                        }
                )

                // Icon container with gradient fill and sweep border
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .drawBehind {
                            // Gradient fill
                            drawCircle(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        surfaceColor,
                                        surfaceColor.copy(alpha = 0.92f)
                                    )
                                )
                            )
                            // Subtle sweep-gradient border
                            drawCircle(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        UNITOred.copy(alpha = 0.18f),
                                        AIRblue.copy(alpha = 0.12f),
                                        AIRyellow.copy(alpha = 0.12f),
                                        UNITOred.copy(alpha = 0.18f)
                                    )
                                ),
                                radius = size.minDimension / 2f,
                                style = Stroke(width = 2.5f.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pepper),
                        contentDescription = "Pepper",
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── Title with gradient text ─────────────────────
            Text(
                text = labels.welcomeTitle,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp,
                    brush = Brush.linearGradient(
                        colors = listOf(UNITOred, granata)
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer {
                    alpha = titleAlpha.value
                    translationY = titleOffsetY.value * density
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Animated decorative divider ──────────────────
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = dividerAlpha.value
                        scaleX = dividerScale.value
                    }
                    .width(120.dp)
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                UNITOred.copy(alpha = 0.8f),
                                granata.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ── Subtitle ─────────────────────────────────────
            Text(
                text = labels.welcomeSubtitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    color = onBackgroundColor.copy(alpha = 0.7f)
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

            // ── CTA button with pulse animation ──────────────
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
                    myIcon = ClientIcons.conversation(),
                    width = 320.dp,
                    height = 80.dp
                )
            }
        }

        // ── Footer branding ──────────────────────────────────
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .graphicsLayer { alpha = footerAlpha.value },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Small decorative dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(UNITOred.copy(alpha = 0.4f), CircleShape)
            )
            Text(
                text = "Pepper ChatBot",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = onBackgroundColor.copy(alpha = 0.35f),
                    letterSpacing = 1.5.sp
                )
            )
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(UNITOred.copy(alpha = 0.4f), CircleShape)
            )
        }
    }
}
