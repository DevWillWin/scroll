package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF001B24),
    primaryContainer = Color(0xFF004D5A),
    onPrimaryContainer = Color(0xFF9CF0FF),
    secondary = ElectricPink,
    onSecondary = Color(0xFF3E001A),
    secondaryContainer = Color(0xFF5E002A),
    onSecondaryContainer = Color(0xFFFFD9E2),
    tertiary = ElectricPurple,
    onTertiary = Color(0xFF2E004F),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = DarkSurfaceHighlight
)

private val LightColorScheme = darkColorScheme(
    // We enforce the deep cinematic theme as ReelScroll is dedicated to media viewing
    primary = NeonCyan,
    onPrimary = Color(0xFF001B24),
    primaryContainer = Color(0xFF004D5A),
    onPrimaryContainer = Color(0xFF9CF0FF),
    secondary = ElectricPink,
    onSecondary = Color(0xFF3E001A),
    secondaryContainer = Color(0xFF5E002A),
    onSecondaryContainer = Color(0xFFFFD9E2),
    tertiary = ElectricPurple,
    onTertiary = Color(0xFF2E004F),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = DarkSurfaceHighlight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
