package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun TimerCountdownRing(
    secondsRemaining: Int,
    totalSeconds: Int,
    isRunning: Boolean,
    isPaused: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp
) {
    val progress = if (totalSeconds > 0) {
        (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_ring")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )

    val activeColor = when {
        isPaused -> WarningAmber
        isRunning -> NeonCyan
        else -> ElectricPink
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .testTag("timer_countdown_ring")
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 10.dp.toPx()
            val radius = (this.size.minDimension - strokeWidth * 2) / 2
            val center = Offset(this.size.width / 2, this.size.height / 2)

            // Background track
            drawCircle(
                color = Color(0x33253148),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Animated Progress arc
            if (isRunning || isPaused) {
                val sweepAngle = progress * 360f
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(NeonCyan, ElectricPink, NeonCyan),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            } else {
                // Idle ready ring
                drawCircle(
                    color = Color(0x4400E5FF),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }

        // Center Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val mins = secondsRemaining / 60
            val secs = secondsRemaining % 60
            val timeString = if (mins > 0) {
                String.format("%d:%02d", mins, secs)
            } else {
                "${secs}s"
            }

            Text(
                text = timeString,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = activeColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            val statusLabel = when {
                isPaused -> "PAUSED"
                isRunning -> "SCROLLING"
                else -> "READY"
            }

            Text(
                text = statusLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                ),
                color = if (isRunning && !isPaused) NeonCyan else TextSecondary
            )
        }
    }
}
