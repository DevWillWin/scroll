package com.example.model

enum class ScrollPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationSeconds: Int,
    val description: String
) {
    FLASH_15S(
        id = "flash_15",
        title = "15 Sec",
        subtitle = "Fast TikToks",
        durationSeconds = 15,
        description = "Quick snippets, memes, and rapid-fire shorts"
    ),
    STANDARD_30S(
        id = "standard_30",
        title = "30 Sec",
        subtitle = "Standard Reel",
        durationSeconds = 30,
        description = "Balanced timing for typical reels and dance trends"
    ),
    ONE_MINUTE(
        id = "one_minute",
        title = "1 Minute",
        subtitle = "Comedy & Stories",
        durationSeconds = 60,
        description = "Ideal for skits, recipes, explanations, and mini-vlogs"
    ),
    TWO_MINUTE(
        id = "two_minute",
        title = "2 Minutes",
        subtitle = "Deep Watch",
        durationSeconds = 120,
        description = "Full educational reels, podcast clips, and deep discussions"
    ),
    ARTICLE_READER(
        id = "article_reader",
        title = "45 Sec",
        subtitle = "Article Reader",
        durationSeconds = 45,
        description = "Gentle reading mode for blogs, Reddit, and long posts"
    ),
    CUSTOM(
        id = "custom",
        title = "Custom",
        subtitle = "Your Pace",
        durationSeconds = 60,
        description = "Set your own exact minutes and seconds"
    )
}

enum class SwipeDirection(val label: String, val description: String) {
    UP("Scroll Down (Next)", "Standard swipe up to show next reel"),
    DOWN("Scroll Up (Previous)", "Swipe down to revisit previous reel"),
    LEFT("Scroll Left", "Swipe left for carousel posts or slides"),
    RIGHT("Scroll Right", "Swipe right for reverse slides")
}

enum class SwipeSpeed(val label: String, val durationMs: Long) {
    FAST("Snappy (200ms)", 200L),
    NORMAL("Natural (350ms)", 350L),
    SMOOTH("Gentle (600ms)", 600L)
}

enum class SwipeDistance(val label: String, val fraction: Float) {
    FULL("Full Page (75%)", 0.75f),
    MEDIUM("Medium (55%)", 0.55f),
    SHORT("Short Peek (35%)", 0.35f)
}

data class AppTarget(
    val id: String,
    val name: String,
    val packageName: String,
    val webFallbackUrl: String,
    val category: String,
    val iconEmoji: String
)

object TargetAppRepository {
    val popularApps = listOf(
        AppTarget(
            id = "tiktok",
            name = "TikTok",
            packageName = "com.zhiliaoapp.musically",
            webFallbackUrl = "https://www.tiktok.com",
            category = "Shorts & Video",
            iconEmoji = "🎵"
        ),
        AppTarget(
            id = "instagram",
            name = "Instagram Reels",
            packageName = "com.instagram.android",
            webFallbackUrl = "https://www.instagram.com/reels",
            category = "Social Media",
            iconEmoji = "📸"
        ),
        AppTarget(
            id = "youtube",
            name = "YouTube Shorts",
            packageName = "com.google.android.youtube",
            webFallbackUrl = "https://www.youtube.com/shorts",
            category = "Video Feed",
            iconEmoji = "▶️"
        ),
        AppTarget(
            id = "reddit",
            name = "Reddit",
            packageName = "com.reddit.frontpage",
            webFallbackUrl = "https://www.reddit.com",
            category = "Discussions & News",
            iconEmoji = "🤖"
        ),
        AppTarget(
            id = "chrome",
            name = "Chrome / Web",
            packageName = "com.android.chrome",
            webFallbackUrl = "https://news.google.com",
            category = "Articles & Browsing",
            iconEmoji = "🌐"
        ),
        AppTarget(
            id = "x",
            name = "X (Twitter)",
            packageName = "com.twitter.android",
            webFallbackUrl = "https://twitter.com",
            category = "Social Feed",
            iconEmoji = "𝕏"
        )
    )
}
