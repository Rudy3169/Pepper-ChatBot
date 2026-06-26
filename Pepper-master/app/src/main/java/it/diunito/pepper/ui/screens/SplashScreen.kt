package it.diunito.pepper.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.diunito.pepper.R
import it.diunito.pepper.ui.theme.AIRblue25

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val progress = remember { Animatable(0f) } // progress bar starting point
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 2000,
                easing = { it }
            )
        )
        onFinished()
    }

    val iconSize = 200.dp
    val arcRadius = 150f       // same center of the icon, but bigger radius

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        // Box containing icon and title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(iconSize + 100.dp),     // spazio per icona + arco
            contentAlignment = Alignment.Center
        ) {
           // Title
            ArcText(
                text = "CHAT WITH PEPPER",
                radiusDp = arcRadius,
                sweepDegrees = 180f,
                clockwise = true,
                color = MaterialTheme.colorScheme.onBackground.toArgb(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .matchParentSize()
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(color = AIRblue25, shape = CircleShape)
                    .padding(18.dp, 20.dp, 0.dp, 0.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_chat),
                    contentDescription = "App icon",
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        // Loading bar
        LinearProgressIndicator(
        progress = { progress.value },
        modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(15.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.secondaryContainer,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}



// -------- Curve Title helper -------- //
@Composable
fun ArcText(
    text: String,
    radiusDp: Float,
    sweepDegrees: Float,
    clockwise: Boolean,
    modifier: Modifier = Modifier,
    color: Int = MaterialTheme.colorScheme.onBackground.toArgb(),
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineLarge,
) {
    val density = LocalDensity.current
    val radiusPx = with(density) { radiusDp.dp.toPx() }
    val textSizePx = with(density) { style.fontSize.toPx() }
    val fontWeight = style.fontWeight ?: FontWeight.Normal
    val typeface = if (fontWeight >= FontWeight.SemiBold)
        android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((radiusDp * 2f).dp)     // bigger than the icon size
    ) {
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                this.color = color
                this.textSize = textSizePx
                this.textAlign = android.graphics.Paint.Align.CENTER
                this.typeface = typeface
            }
            val rect = android.graphics.RectF(
                center.x - radiusPx,
                center.y - radiusPx,
                center.x + radiusPx,
                center.y + radiusPx
            )
            val start = if (clockwise) 180f + (180f - sweepDegrees) / 2f
            else (180f - sweepDegrees) / 2f
            val sweep = if (clockwise) sweepDegrees else -sweepDegrees
            val path = android.graphics.Path().apply { addArc(rect, start, sweep) }

            canvas.nativeCanvas.drawTextOnPath(text, path, 0f, 0f, paint)
        }
    }
}
