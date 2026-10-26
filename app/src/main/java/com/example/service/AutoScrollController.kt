package com.example.service

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.data.PreferencesManager
import com.example.model.ScrollPreset
import com.example.model.SwipeDirection
import com.example.model.SwipeDistance
import com.example.model.SwipeSpeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class AutoScrollState(
    val isServiceConnected: Boolean = false,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val secondsRemaining: Int = 60,
    val totalIntervalSeconds: Int = 60,
    val scrollCountSession: Int = 0,
    val scrollCountLifetime: Int = 0,
    val selectedPreset: ScrollPreset = ScrollPreset.ONE_MINUTE,
    val customSeconds: Int = 60,
    val swipeDirection: SwipeDirection = SwipeDirection.UP,
    val swipeSpeed: SwipeSpeed = SwipeSpeed.NORMAL,
    val swipeDistance: SwipeDistance = SwipeDistance.FULL,
    val hapticFeedback: Boolean = true,
    val soundChime: Boolean = true,
    val overlayVisible: Boolean = true,
    val overlayMinimized: Boolean = false,
    val statusMessage: String = "Ready"
)

object AutoScrollController {
    private val _state = MutableStateFlow(AutoScrollState())
    val state: StateFlow<AutoScrollState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    private var prefsManager: PreferencesManager? = null
    private var appContext: Context? = null
    private var toneGenerator: ToneGenerator? = null

    fun initialize(context: Context) {
        val appCtx = context.applicationContext
        appContext = appCtx
        val prefs = PreferencesManager(appCtx)
        prefsManager = prefs

        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            // ToneGenerator fallback
        }

        val effectiveDuration = prefs.getEffectiveDurationSeconds()
        _state.update {
            it.copy(
                selectedPreset = prefs.selectedPreset,
                customSeconds = prefs.customSeconds,
                totalIntervalSeconds = effectiveDuration,
                secondsRemaining = effectiveDuration,
                swipeDirection = prefs.swipeDirection,
                swipeSpeed = prefs.swipeSpeed,
                swipeDistance = prefs.swipeDistance,
                hapticFeedback = prefs.hapticFeedbackEnabled,
                soundChime = prefs.soundChimeEnabled,
                overlayVisible = prefs.floatingOverlayEnabled,
                scrollCountLifetime = prefs.totalScrollsCount,
                isServiceConnected = AutoScrollAccessibilityService.isServiceRunning()
            )
        }
    }

    fun setServiceConnected(connected: Boolean) {
        _state.update { it.copy(isServiceConnected = connected) }
    }

    fun selectPreset(preset: ScrollPreset) {
        val prefs = prefsManager ?: return
        prefs.selectedPreset = preset
        val duration = if (preset == ScrollPreset.CUSTOM) prefs.customSeconds else preset.durationSeconds
        _state.update {
            it.copy(
                selectedPreset = preset,
                totalIntervalSeconds = duration,
                secondsRemaining = if (it.isRunning) duration else duration
            )
        }
    }

    fun setCustomSeconds(seconds: Int) {
        val prefs = prefsManager ?: return
        val clamped = seconds.coerceIn(3, 3600)
        prefs.customSeconds = clamped
        if (_state.value.selectedPreset == ScrollPreset.CUSTOM) {
            _state.update {
                it.copy(
                    customSeconds = clamped,
                    totalIntervalSeconds = clamped,
                    secondsRemaining = if (it.isRunning) clamped else clamped
                )
            }
        } else {
            _state.update { it.copy(customSeconds = clamped) }
        }
    }

    fun setSwipeDirection(direction: SwipeDirection) {
        prefsManager?.swipeDirection = direction
        _state.update { it.copy(swipeDirection = direction) }
    }

    fun setSwipeSpeed(speed: SwipeSpeed) {
        prefsManager?.swipeSpeed = speed
        _state.update { it.copy(swipeSpeed = speed) }
    }

    fun setSwipeDistance(distance: SwipeDistance) {
        prefsManager?.swipeDistance = distance
        _state.update { it.copy(swipeDistance = distance) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        prefsManager?.hapticFeedbackEnabled = enabled
        _state.update { it.copy(hapticFeedback = enabled) }
    }

    fun setSoundChime(enabled: Boolean) {
        prefsManager?.soundChimeEnabled = enabled
        _state.update { it.copy(soundChime = enabled) }
    }

    fun setOverlayVisible(visible: Boolean) {
        prefsManager?.floatingOverlayEnabled = visible
        _state.update { it.copy(overlayVisible = visible) }
        val service = AutoScrollAccessibilityService.instance
        if (service != null) {
            if (visible && _state.value.isRunning) {
                service.showFloatingOverlay()
            } else if (!visible) {
                service.hideFloatingOverlay()
            }
        }
    }

    fun toggleOverlayMinimized() {
        _state.update { it.copy(overlayMinimized = !it.overlayMinimized) }
        AutoScrollAccessibilityService.instance?.updateOverlayUI()
    }

    fun startAutoScroll(): Boolean {
        val totalSecs = _state.value.totalIntervalSeconds
        _state.update {
            it.copy(
                isRunning = true,
                isPaused = false,
                secondsRemaining = totalSecs,
                statusMessage = "Auto-scrolling every ${formatTime(totalSecs)}"
            )
        }

        // Show floating overlay if accessibility service is active and overlay is enabled
        if (_state.value.overlayVisible) {
            AutoScrollAccessibilityService.instance?.showFloatingOverlay()
        }

        startTimerLoop()
        return true
    }

    fun pauseAutoScroll() {
        timerJob?.cancel()
        timerJob = null
        _state.update {
            it.copy(
                isPaused = true,
                statusMessage = "Paused"
            )
        }
        AutoScrollAccessibilityService.instance?.updateOverlayUI()
    }

    fun resumeAutoScroll() {
        if (!_state.value.isRunning) {
            startAutoScroll()
            return
        }
        _state.update {
            it.copy(
                isPaused = false,
                statusMessage = "Resumed"
            )
        }
        startTimerLoop()
        AutoScrollAccessibilityService.instance?.updateOverlayUI()
    }

    fun togglePlayPause() {
        if (!_state.value.isRunning) {
            startAutoScroll()
        } else if (_state.value.isPaused) {
            resumeAutoScroll()
        } else {
            pauseAutoScroll()
        }
    }

    fun stopAutoScroll() {
        timerJob?.cancel()
        timerJob = null
        val totalSecs = _state.value.totalIntervalSeconds
        _state.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                secondsRemaining = totalSecs,
                statusMessage = "Stopped"
            )
        }
        AutoScrollAccessibilityService.instance?.hideFloatingOverlay()
    }

    fun triggerImmediateScroll(directionOverride: SwipeDirection? = null) {
        val dir = directionOverride ?: _state.value.swipeDirection
        executeSwipe(dir)
        // Reset timer countdown
        _state.update {
            it.copy(secondsRemaining = it.totalIntervalSeconds)
        }
        AutoScrollAccessibilityService.instance?.updateOverlayUI()
    }

    fun triggerPreviousScroll() {
        // Reverse of current direction
        val reverseDir = when (_state.value.swipeDirection) {
            SwipeDirection.UP -> SwipeDirection.DOWN
            SwipeDirection.DOWN -> SwipeDirection.UP
            SwipeDirection.LEFT -> SwipeDirection.RIGHT
            SwipeDirection.RIGHT -> SwipeDirection.LEFT
        }
        executeSwipe(reverseDir)
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive && _state.value.isRunning && !_state.value.isPaused) {
                delay(1000L)
                val currentRem = _state.value.secondsRemaining
                if (currentRem <= 1) {
                    // Trigger scroll!
                    executeSwipe(_state.value.swipeDirection)
                    val nextTotal = _state.value.totalIntervalSeconds
                    _state.update { it.copy(secondsRemaining = nextTotal) }
                } else {
                    _state.update { it.copy(secondsRemaining = currentRem - 1) }
                }
                AutoScrollAccessibilityService.instance?.updateOverlayUI()
            }
        }
    }

    private fun executeSwipe(direction: SwipeDirection) {
        val currentState = _state.value
        val service = AutoScrollAccessibilityService.instance

        if (currentState.soundChime) {
            playChime()
        }
        if (currentState.hapticFeedback) {
            playHaptic()
        }

        if (service != null && AutoScrollAccessibilityService.isServiceRunning()) {
            service.performSwipe(
                direction = direction,
                distanceFraction = currentState.swipeDistance.fraction,
                durationMs = currentState.swipeSpeed.durationMs
            )
        }

        prefsManager?.incrementScrollCount()
        val newLifetime = (prefsManager?.totalScrollsCount) ?: (currentState.scrollCountLifetime + 1)
        _state.update {
            it.copy(
                scrollCountSession = it.scrollCountSession + 1,
                scrollCountLifetime = newLifetime
            )
        }
    }

    private fun playChime() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun playHaptic() {
        val ctx = appContext ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(35L, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(35L, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(35L)
                }
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return if (mins > 0) {
            if (secs > 0) "${mins}m ${secs}s" else "${mins}m"
        } else {
            "${secs}s"
        }
    }
}
