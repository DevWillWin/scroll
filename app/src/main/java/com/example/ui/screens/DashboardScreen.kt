package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScrollPreset
import com.example.model.SwipeDirection
import com.example.model.SwipeDistance
import com.example.model.SwipeSpeed
import com.example.service.AutoScrollAccessibilityService
import com.example.service.AutoScrollController
import com.example.ui.components.AppLauncherSection
import com.example.ui.components.TimerCountdownRing
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.GlowGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToSimulator: () -> Unit,
    onNavigateToGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by AutoScrollController.state.collectAsState()
    val context = LocalContext.current
    var isSettingsExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            HeaderSection(
                lifetimeScrolls = state.scrollCountLifetime,
                sessionScrolls = state.scrollCountSession
            )
        }

        item {
            AccessibilityStatusCard(
                isServiceConnected = state.isServiceConnected,
                onEnableClick = {
                    AutoScrollAccessibilityService.openAccessibilitySettings(context)
                },
                onLearnMore = onNavigateToGuide
            )
        }

        item {
            PresetSelectorSection(
                selectedPreset = state.selectedPreset,
                customSeconds = state.customSeconds,
                onPresetSelected = { AutoScrollController.selectPreset(it) },
                onCustomSecondsChanged = { AutoScrollController.setCustomSeconds(it) }
            )
        }

        item {
            HeroControlCard(
                state = state,
                onTogglePlayPause = { AutoScrollController.togglePlayPause() },
                onStop = { AutoScrollController.stopAutoScroll() },
                onSkipNext = { AutoScrollController.triggerImmediateScroll() },
                onSkipPrev = { AutoScrollController.triggerPreviousScroll() }
            )
        }

        item {
            FloatingOverlayToggleCard(
                overlayVisible = state.overlayVisible,
                onToggle = { AutoScrollController.setOverlayVisible(it) }
            )
        }

        item {
            GestureSettingsCard(
                state = state,
                isExpanded = isSettingsExpanded,
                onToggleExpand = { isSettingsExpanded = !isSettingsExpanded }
            )
        }

        item {
            AppLauncherSection(
                onAppSelected = { /* App launched */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderSection(
    lifetimeScrolls: Int,
    sessionScrolls: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "ReelScroll",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                ),
                color = TextPrimary
            )
            Text(
                text = "Hands-free video & article auto-scroll",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        Surface(
            color = DarkSurfaceVariant,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3300E5FF))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$lifetimeScrolls scrolls",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonCyan
                )
            }
        }
    }
}

@Composable
private fun AccessibilityStatusCard(
    isServiceConnected: Boolean,
    onEnableClick: () -> Unit,
    onLearnMore: () -> Unit
) {
    val borderColor by animateColorAsState(
        if (isServiceConnected) GlowGreen else WarningAmber,
        label = "status_border"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("accessibility_status_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = if (isServiceConnected) GlowGreen else WarningAmber,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isServiceConnected) "Service Ready" else "Accessibility Setup Needed",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isServiceConnected) GlowGreen else WarningAmber
                    )
                }

                if (!isServiceConnected) {
                    Text(
                        text = "How to enable?",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan,
                        modifier = Modifier
                            .clickable(onClick = onLearnMore)
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isServiceConnected) {
                    "Accessibility gesture service is active. Auto-scroll will work over TikTok, Reels, YouTube Shorts, and any other app."
                } else {
                    "Android requires Accessibility permission so ReelScroll can simulate touch swipes across other apps. Tap below to enable it in Settings."
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            if (!isServiceConnected) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onEnableClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("enable_accessibility_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarningAmber,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enable ReelScroll in Settings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PresetSelectorSection(
    selectedPreset: ScrollPreset,
    customSeconds: Int,
    onPresetSelected: (ScrollPreset) -> Unit,
    onCustomSecondsChanged: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Scroll Interval",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = selectedPreset.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Preset Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetChip(
                preset = ScrollPreset.FLASH_15S,
                isSelected = selectedPreset == ScrollPreset.FLASH_15S,
                onClick = { onPresetSelected(ScrollPreset.FLASH_15S) },
                modifier = Modifier.weight(1f)
            )
            PresetChip(
                preset = ScrollPreset.STANDARD_30S,
                isSelected = selectedPreset == ScrollPreset.STANDARD_30S,
                onClick = { onPresetSelected(ScrollPreset.STANDARD_30S) },
                modifier = Modifier.weight(1f)
            )
            PresetChip(
                preset = ScrollPreset.ONE_MINUTE,
                isSelected = selectedPreset == ScrollPreset.ONE_MINUTE,
                onClick = { onPresetSelected(ScrollPreset.ONE_MINUTE) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetChip(
                preset = ScrollPreset.TWO_MINUTE,
                isSelected = selectedPreset == ScrollPreset.TWO_MINUTE,
                onClick = { onPresetSelected(ScrollPreset.TWO_MINUTE) },
                modifier = Modifier.weight(1f)
            )
            PresetChip(
                preset = ScrollPreset.ARTICLE_READER,
                isSelected = selectedPreset == ScrollPreset.ARTICLE_READER,
                onClick = { onPresetSelected(ScrollPreset.ARTICLE_READER) },
                modifier = Modifier.weight(1f)
            )
            PresetChip(
                preset = ScrollPreset.CUSTOM,
                isSelected = selectedPreset == ScrollPreset.CUSTOM,
                onClick = { onPresetSelected(ScrollPreset.CUSTOM) },
                modifier = Modifier.weight(1f)
            )
        }

        // Custom time editor
        AnimatedVisibility(visible = selectedPreset == ScrollPreset.CUSTOM) {
            CustomTimerEditor(
                customSeconds = customSeconds,
                onSecondsChanged = onCustomSecondsChanged
            )
        }
    }
}

@Composable
private fun PresetChip(
    preset: ScrollPreset,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) {
        Brush.horizontalGradient(listOf(Color(0xFF00B4D8), Color(0xFFFF2A85)))
    } else {
        Brush.horizontalGradient(listOf(DarkSurfaceVariant, DarkSurfaceVariant))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) NeonCyan else Color(0x33253148),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .testTag("preset_${preset.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = preset.title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) Color.White else TextPrimary
            )
            Text(
                text = preset.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = if (isSelected) Color(0xFFE0F7FA) else TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CustomTimerEditor(
    customSeconds: Int,
    onSecondsChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4400E5FF))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Custom Duration",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                val mins = customSeconds / 60
                val secs = customSeconds % 60
                Text(
                    text = if (mins > 0) "${mins}m ${secs}s ($customSeconds sec)" else "$customSeconds sec",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                    color = NeonCyan
                )
            }

            Slider(
                value = customSeconds.toFloat(),
                onValueChange = { onSecondsChanged(it.toInt()) },
                valueRange = 5f..300f,
                steps = 58,
                colors = SliderDefaults.colors(
                    thumbColor = NeonCyan,
                    activeTrackColor = NeonCyan,
                    inactiveTrackColor = DarkSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 30, 60, 90, 120, 180).forEach { sec ->
                    FilledTonalButton(
                        onClick = { onSecondsChanged(sec) },
                        modifier = Modifier.weight(1f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (sec >= 60) "${sec / 60}m" else "${sec}s",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroControlCard(
    state: com.example.service.AutoScrollState,
    onTogglePlayPause: () -> Unit,
    onStop: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrev: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(20.dp))
            .testTag("hero_control_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerCountdownRing(
                secondsRemaining = state.secondsRemaining,
                totalSeconds = state.totalIntervalSeconds,
                isRunning = state.isRunning,
                isPaused = state.isPaused,
                size = 180.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Primary Play/Pause Button
            val buttonGradient = if (state.isRunning && !state.isPaused) {
                Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFFFF2A85)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(buttonGradient)
                    .clickable(onClick = onTogglePlayPause)
                    .testTag("main_action_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when {
                            !state.isRunning -> Icons.Default.PlayArrow
                            state.isPaused -> Icons.Default.PlayArrow
                            else -> Icons.Default.Pause
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            !state.isRunning -> "Start Auto-Scroll"
                            state.isPaused -> "Resume Auto-Scroll"
                            else -> "Pause Auto-Scroll"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSkipPrev,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("prev_reel_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Reel",
                        tint = TextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Prev Reel", color = TextPrimary, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onSkipNext,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("next_reel_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Reel",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Next Reel", color = NeonCyan, fontSize = 12.sp)
                }

                if (state.isRunning) {
                    IconButton(
                        onClick = onStop,
                        modifier = Modifier
                            .background(Color(0x33FF2A85), CircleShape)
                            .testTag("stop_scroll_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = ElectricPink
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingOverlayToggleCard(
    overlayVisible: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x2200E5FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Floating On-Screen Controls",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Draggable mini timer pill over other apps",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Switch(
                checked = overlayVisible,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = Color(0xFF004D5A)
                ),
                modifier = Modifier.testTag("floating_overlay_switch")
            )
        }
    }
}

@Composable
private fun GestureSettingsCard(
    state: com.example.service.AutoScrollState,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = ElectricPink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Gesture & Feedback Settings",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${state.swipeDirection.label} • ${state.swipeSpeed.label}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                Text(
                    text = if (isExpanded) "Hide" else "Customize",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // Direction Selection
                    Text(
                        text = "Swipe Direction",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SwipeDirection.values().forEach { dir ->
                            val selected = state.swipeDirection == dir
                            FilledTonalButton(
                                onClick = { AutoScrollController.setSwipeDirection(dir) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(2.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (selected) NeonCyan else DarkSurfaceVariant,
                                    contentColor = if (selected) Color.Black else TextPrimary
                                )
                            ) {
                                Text(
                                    text = when (dir) {
                                        SwipeDirection.UP -> "Down (Next)"
                                        SwipeDirection.DOWN -> "Up (Prev)"
                                        SwipeDirection.LEFT -> "Left"
                                        SwipeDirection.RIGHT -> "Right"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Speed Selection
                    Text(
                        text = "Swipe Speed",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SwipeSpeed.values().forEach { speed ->
                            val selected = state.swipeSpeed == speed
                            FilledTonalButton(
                                onClick = { AutoScrollController.setSwipeSpeed(speed) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (selected) ElectricPink else DarkSurfaceVariant,
                                    contentColor = if (selected) Color.White else TextPrimary
                                )
                            ) {
                                Text(speed.label.substringBefore(" ("), fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sound & Haptic Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Sound Chime on Scroll", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Text("Plays gentle futuristic audio chime", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Switch(
                            checked = state.soundChime,
                            onCheckedChange = { AutoScrollController.setSoundChime(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Vibration Feedback", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Text("Subtle haptic tick when reel advances", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Switch(
                            checked = state.hapticFeedback,
                            onCheckedChange = { AutoScrollController.setHapticFeedback(it) }
                        )
                    }
                }
            }
        }
    }
}
