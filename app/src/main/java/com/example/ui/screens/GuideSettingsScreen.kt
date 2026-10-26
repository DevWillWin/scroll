package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.service.AutoScrollAccessibilityService
import com.example.service.AutoScrollController
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.GlowGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun GuideSettingsScreen(
    modifier: Modifier = Modifier
) {
    val state by AutoScrollController.state.collectAsState()
    val context = LocalContext.current
    var expandedStep by remember { mutableStateOf<Int?>(1) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Setup & Permissions",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                ),
                color = TextPrimary
            )
            Text(
                text = "How ReelScroll scrolls TikTok, Instagram, and any app hands-free",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        // Live Permission Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (state.isServiceConnected) GlowGreen.copy(alpha = 0.5f) else WarningAmber.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (state.isServiceConnected) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (state.isServiceConnected) GlowGreen else WarningAmber,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (state.isServiceConnected) "Accessibility Service Enabled" else "Service Not Enabled Yet",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (state.isServiceConnected) "System gestures permitted" else "Required for auto-scrolling",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            AutoScrollAccessibilityService.openAccessibilitySettings(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_accessibility_settings_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isServiceConnected) DarkSurfaceVariant else WarningAmber,
                            contentColor = if (state.isServiceConnected) NeonCyan else Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.isServiceConnected) "Open Accessibility Settings" else "Turn On Accessibility Service",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Test Gesture Button Sandbox
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3300E5FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Test Gesture on Screen",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Trigger a 1-time swipe up to verify your device dispatches gestures smoothly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (state.isServiceConnected) {
                                    AutoScrollController.triggerImmediateScroll()
                                    Toast.makeText(context, "Test swipe dispatched!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please enable Accessibility Service first", Toast.LENGTH_LONG).show()
                                    AutoScrollAccessibilityService.openAccessibilitySettings(context)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("test_gesture_sandbox_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Swipe", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                if (state.isServiceConnected) {
                                    AutoScrollController.triggerImmediateScratch()
                                    Toast.makeText(context, "Gift card scratch dispatched in center!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please enable Accessibility Service first", Toast.LENGTH_LONG).show()
                                    AutoScrollAccessibilityService.openAccessibilitySettings(context)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("test_scratch_sandbox_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB703))
                        ) {
                            Text("🪙", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Scratch", color = Color(0xFFFFB703), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Step by Step Setup Instructions
        item {
            Text(
                text = "Step-by-Step Setup Guide",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            StepCard(
                stepNumber = 1,
                title = "Open Android Settings",
                description = "Tap the 'Turn On Accessibility' button above to jump directly to Android's Accessibility settings menu.",
                tip = "On Samsung: Look under 'Installed apps'. On Pixel: Look under 'Downloaded apps'.",
                isExpanded = expandedStep == 1,
                onToggle = { expandedStep = if (expandedStep == 1) null else 1 }
            )
        }

        item {
            StepCard(
                stepNumber = 2,
                title = "Select 'ReelScroll Service'",
                description = "Scroll through the list of downloaded services until you find 'ReelScroll' or 'ReelScroll Service'.",
                tip = "If asked about full control: Android displays this standard dialog for all apps that simulate touch gestures.",
                isExpanded = expandedStep == 2,
                onToggle = { expandedStep = if (expandedStep == 2) null else 2 }
            )
        }

        item {
            StepCard(
                stepNumber = 3,
                title = "Toggle Switch ON",
                description = "Turn the switch ON and tap 'Allow'. The service is now live and ready to scroll for you.",
                tip = "You can return to ReelScroll right after toggling it ON.",
                isExpanded = expandedStep == 3,
                onToggle = { expandedStep = if (expandedStep == 3) null else 3 }
            )
        }

        item {
            StepCard(
                stepNumber = 4,
                title = "Choose 1 Min, 2 Min or Custom",
                description = "Choose how long you want each reel to play before advancing. Tap Start or launch TikTok/Instagram from our quick launcher!",
                tip = "The floating on-screen controller allows you to pause or skip without returning to this app.",
                isExpanded = expandedStep == 4,
                onToggle = { expandedStep = if (expandedStep == 4) null else 4 }
            )
        }

        // Pro Tips & Use Cases
        item {
            Text(
                text = "Popular Use Cases",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            UseCaseCard(
                title = "🍳 Cooking in the Kitchen",
                recommendedTimer = "1 or 2 Minutes",
                description = "Set your phone on the counter while cooking or baking. Recipes and reels scroll automatically without getting flour or oil on your screen!"
            )
        }

        item {
            UseCaseCard(
                title = "🏋️ Gym & Workout Routines",
                recommendedTimer = "45s - 1 Minute",
                description = "Follow fitness shorts, HIIT timer reels, or mobility clips hands-free between workout sets."
            )
        }

        item {
            UseCaseCard(
                title = "📖 Non-Social Reading & Blogs",
                recommendedTimer = "30s - 45s (35% peek)",
                description = "Set to Article Reader mode with 35% distance. Reads Reddit threads, news articles, and ebooks like a teleprompter."
            )
        }

        // Lifetime Stats Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Lifetime Activity",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(label = "Total Scrolls", value = "${state.scrollCountLifetime}")
                        StatItem(label = "This Session", value = "${state.scrollCountSession}")
                        val approxHours = String.format("%.1f", (state.scrollCountLifetime * state.totalIntervalSeconds) / 3600f)
                        StatItem(label = "Hours Watched", value = "$approxHours h")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StepCard(
    stepNumber: Int,
    title: String,
    description: String,
    tip: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onToggle),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(0x3300E5FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$stepNumber",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tip,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UseCaseCard(
    title: String,
    recommendedTimer: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Surface(
                    color = Color(0x33FF2A85),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = recommendedTimer,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = ElectricPink,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
            color = NeonCyan
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondary
        )
    }
}
