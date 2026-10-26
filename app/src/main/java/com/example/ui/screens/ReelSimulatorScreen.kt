package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AutoScrollController
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class SimulatedReel(
    val id: Int,
    val creatorName: String,
    val creatorHandle: String,
    val caption: String,
    val hashtags: String,
    val songTitle: String,
    val likesCount: String,
    val commentsCount: String,
    val sharesCount: String,
    val gradientColors: List<Color>,
    val emojiArt: String
)

private val sampleReels = listOf(
    SimulatedReel(
        id = 1,
        creatorName = "Chef Elena 🍳",
        creatorHandle = "@elena_bakes",
        caption = "Crispy Garlic Parmesan Butter Smashed Potatoes! The crunch at the end is unbelievable 🔥",
        hashtags = "#foodtok #potatotiktok #recipes #comfortfood",
        songTitle = "Lofi Cooking Beats • Original Sound",
        likesCount = "184.2K",
        commentsCount = "1,420",
        sharesCount = "29.8K",
        gradientColors = listOf(Color(0xFF2E0854), Color(0xFFE94560), Color(0xFF0F3460)),
        emojiArt = "🥔 🧄 🧀"
    ),
    SimulatedReel(
        id = 2,
        creatorName = "CyberTech Labs 🤖",
        creatorHandle = "@cybertech_future",
        caption = "Top 5 AI tools in 2026 that actually automate your entire daily workflow hands-free 💻✨",
        hashtags = "#techtrends #productivity #futuretech #gadgets",
        songTitle = "Cyberpunk Synthwave 2077 • Synth Bass",
        likesCount = "342.1K",
        commentsCount = "2,890",
        sharesCount = "81.4K",
        gradientColors = listOf(Color(0xFF0D1B2A), Color(0xFF0077B6), Color(0xFF00B4D8)),
        emojiArt = "⚡ 🦾 🌐"
    ),
    SimulatedReel(
        id = 3,
        creatorName = "Leo & Maya (Samoyed) 🐶",
        creatorHandle = "@fluffy_samoyeds",
        caption = "He thought we were going to the vet... then we pulled up to the dog park beach! Wait for his zoomies 🏖️🐾",
        hashtags = "#dogsoftiktok #samoyed #puppylove #zoomies",
        songTitle = "Happy Whistle Melody • Acoustic Chill",
        likesCount = "620.8K",
        commentsCount = "4,110",
        sharesCount = "94.3K",
        gradientColors = listOf(Color(0xFF3A0CA3), Color(0xFF4361EE), Color(0xFF4CC9F0)),
        emojiArt = "🐕 🌊 🦴"
    ),
    SimulatedReel(
        id = 4,
        creatorName = "Tokyo Wanderer 🏮",
        creatorHandle = "@shibuya_vibes",
        caption = "Hidden alleyway ramen stall open at 3 AM in Shinjuku. Rich tonkotsu broth boiled for 36 hours 🍜",
        hashtags = "#tokyo #travelreels #japan #nightwalks",
        songTitle = "Midnight Tokyo Rain • Lo-fi Hip Hop",
        likesCount = "415.9K",
        commentsCount = "3,012",
        sharesCount = "52.1K",
        gradientColors = listOf(Color(0xFF1F1D36), Color(0xFF864879), Color(0xFFE9A6A6)),
        emojiArt = "🍜 🏮 🌧️"
    ),
    SimulatedReel(
        id = 5,
        creatorName = "Workout With Zack ⚡",
        creatorHandle = "@zack_athletics",
        caption = "Fix your posture in 60 seconds! Do these 3 wall slides daily to unlock tight chest & shoulders 💪",
        hashtags = "#mobility #fitnessmotivation #posturecheck #stretching",
        songTitle = "Bass Boosted Workout Pump • Phonk Anthem",
        likesCount = "278.4K",
        commentsCount = "1,980",
        sharesCount = "47.6K",
        gradientColors = listOf(Color(0xFF14213D), Color(0xFFFCA311), Color(0xFF000000)),
        emojiArt = "🏋️ 🔥 ⏱️"
    )
)

private val sampleArticles = listOf(
    "How Autonomous Systems and Adaptive Interfaces Are Reshaping the Modern Smartphone Experience in 2026.",
    "Smartphones have evolved from passive handheld displays into ambient context-aware computing hubs. Today's users consume vast streams of vertical video, news feeds, research documentation, and interactive threads.",
    "The challenge, however, remains physical friction. Holding a device steady during long recipe tutorials, study podcasts, or gym workouts causes wrist strain and interrupts the natural flow of viewing.",
    "Hands-free scrolling algorithms address this by observing pacing intervals. By matching scroll velocities to average reading speeds (roughly 220 words per minute for articles, and 30-90 seconds for vertical reels), hands-free software creates an effortless viewing flow.",
    "Whether you are practicing guitar chords following a tab, washing dishes while catching up on tech news, or binge-watching short-form comedy, timer-based auto-scroll keeps the content flowing seamlessly.",
    "With configurable distance fractions and gentle spring physics, users can fine-tune every swipe to match their preferred reading and viewing rhythms without touching the glass."
)

@Composable
fun ReelSimulatorScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Social Reels, 1 = Article Reader
    val state by AutoScrollController.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Mode Selector Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonCyan,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = if (selectedTab == 0) NeonCyan else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Reels / Shorts Feed",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) NeonCyan else TextSecondary
                        )
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = null,
                            tint = if (selectedTab == 1) ElectricPink else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Article Reader Mode",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) ElectricPink else TextSecondary
                        )
                    }
                }
            )
        }

        if (selectedTab == 0) {
            ReelFeedSimulator(state = state)
        } else {
            ArticleReaderSimulator(state = state)
        }
    }
}

@Composable
private fun ReelFeedSimulator(state: com.example.service.AutoScrollState) {
    val pagerState = rememberPagerState(pageCount = { sampleReels.size })
    val coroutineScope = rememberCoroutineScope()
    var lastScrollCount by remember { mutableIntStateOf(state.scrollCountSession) }

    // When the auto-scroll timer ticks a reel advancement, animate to next page!
    LaunchedEffect(state.scrollCountSession) {
        if (state.scrollCountSession != lastScrollCount) {
            lastScrollCount = state.scrollCountSession
            val nextPage = (pagerState.currentPage + 1) % sampleReels.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("reel_vertical_pager")
        ) { page ->
            val reel = sampleReels[page]
            ReelCard(reel = reel, onNextReel = {
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % sampleReels.size
                    pagerState.animateScrollToPage(nextPage)
                }
            })
        }

        // Center Gift Card Scratching Action Overlay (triggers after every 3 scrolls or on demand)
        CenterScratchCardOverlay(
            isScratching = state.isScratchingNow,
            modifier = Modifier.align(Alignment.Center)
        )

        // Floating Simulator Overlay Header & Timer
        SimulatorOverlayBar(
            state = state,
            onToggleTimer = { AutoScrollController.togglePlayPause() },
            onSkipReel = {
                AutoScrollController.triggerImmediateScroll()
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
private fun SimulatorOverlayBar(
    state: com.example.service.AutoScrollState,
    onToggleTimer: () -> Unit,
    onSkipReel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = Color(0xD0101524),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x5500E5FF)),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.radialGradient(listOf(NeonCyan, Color.Transparent)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val mins = state.secondsRemaining / 60
                    val secs = state.secondsRemaining % 60
                    Text(
                        text = if (mins > 0) "${mins}m" else "${secs}s",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = if (state.isRunning && !state.isPaused) "Auto-Scroll: Active" else "Auto-Scroll: Idle",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (state.isRunning && !state.isPaused) NeonCyan else TextSecondary
                    )
                    Text(
                        text = "Interval: ${state.selectedPreset.title} (${state.totalIntervalSeconds}s)",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = TextPrimary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalButton(
                    onClick = onToggleTimer,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("simulator_toggle_button")
                ) {
                    Icon(
                        imageVector = if (state.isRunning && !state.isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isRunning && !state.isPaused) "Pause" else "Run",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onSkipReel,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0x3300E5FF), CircleShape)
                ) {
                    Text("⏭", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ReelCard(
    reel: SimulatedReel,
    onNextReel: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "reel_ambient")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_drift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = reel.gradientColors
                )
            )
            .padding(bottom = 70.dp)
    ) {
        // Decorative center video simulation canvas
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = waveOffset.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = reel.emojiArt,
                fontSize = 80.sp
            )
        }

        // Dark gradient scrim at bottom for text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000), Color(0xF5000000))
                    )
                )
        )

        // Bottom Left Creator Info & Caption
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = NeonCyan,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👤", fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = reel.creatorName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = reel.creatorHandle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color(0xFFB0BEC5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reel.caption,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = reel.hashtags,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = NeonCyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Audio Pill
            Surface(
                color = Color(0x44FFFFFF),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reel.songTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = Color.White
                    )
                }
            }
        }

        // Right Action Bar (Likes, Comments, Shares)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { isLiked = !isLiked },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0x33000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = if (isLiked) ElectricPink else Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = reel.likesCount,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color.White
                )
            }

            // Comments Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { /* simulated comments */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0x33000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = reel.commentsCount,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color.White
                )
            }

            // Share Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { /* simulated share */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0x33000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = reel.sharesCount,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ArticleReaderSimulator(state: com.example.service.AutoScrollState) {
    val listState = rememberLazyListState()
    var lastScrollCount by remember { mutableIntStateOf(state.scrollCountSession) }

    LaunchedEffect(state.scrollCountSession) {
        if (state.scrollCountSession != lastScrollCount) {
            lastScrollCount = state.scrollCountSession
            val currentFirst = listState.firstVisibleItemIndex
            val nextItem = (currentFirst + 1).coerceAtMost(sampleArticles.size - 1)
            listState.animateScrollToItem(nextItem)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Tech & Science Review • Hands-Free Reader",
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sampleArticles[0],
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("By Dr. A. Vance", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("5 min read", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x33253148))
                )
            }

            items(sampleArticles.drop(1)) { paragraph ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
                ) {
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Overlay Banner
        SimulatorOverlayBar(
            state = state,
            onToggleTimer = { AutoScrollController.togglePlayPause() },
            onSkipReel = { AutoScrollController.triggerImmediateScroll() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun CenterScratchCardOverlay(
    isScratching: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isScratching,
        enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(initialScale = 0.8f),
        exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut(targetScale = 1.1f),
        modifier = modifier
    ) {
        val transition = rememberInfiniteTransition(label = "scrub_anim")
        val scrubOffsetX by transition.animateFloat(
            initialValue = -55f,
            targetValue = 55f,
            animationSpec = infiniteRepeatable(
                animation = tween(120, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scrub_x"
        )
        val scrubOffsetY by transition.animateFloat(
            initialValue = -15f,
            targetValue = 15f,
            animationSpec = infiniteRepeatable(
                animation = tween(180, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scrub_y"
        )

        Card(
            modifier = Modifier
                .width(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, Color(0xFFFFB703), RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xF2101524)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🪙", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "3-SCROLL SCRATCH!",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = Color(0xFFFFB703)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated Scratch Foil Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6C757D), Color(0xFFADB5BD), Color(0xFF495057), Color(0xFFCED4DA))
                            )
                        )
                        .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Revealed Prize under foil
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🎉 BONUS UNLOCKED! ✨",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF1E1B4B)
                        )
                        Text(
                            text = "Gift Card Scratched",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Color(0xFF2E0854)
                        )
                    }

                    // Vigorous scratching coin overlay moving back and forth
                    Box(
                        modifier = Modifier
                            .offset(x = scrubOffsetX.dp, y = scrubOffsetY.dp)
                            .size(36.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFFFD166), Color(0xFFF77F00))
                                ),
                                shape = CircleShape
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Center screen scratch gesture active",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFFFFD166)
                )
            }
        }
    }
}

