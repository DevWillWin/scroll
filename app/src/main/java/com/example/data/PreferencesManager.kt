package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ScrollPreset
import com.example.model.SwipeDirection
import com.example.model.SwipeDistance
import com.example.model.SwipeSpeed

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("reel_scroll_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PRESET = "selected_preset"
        private const val KEY_CUSTOM_SECONDS = "custom_seconds"
        private const val KEY_SWIPE_DIRECTION = "swipe_direction"
        private const val KEY_SWIPE_SPEED = "swipe_speed"
        private const val KEY_SWIPE_DISTANCE = "swipe_distance"
        private const val KEY_SOUND_CHIME = "sound_chime"
        private const val KEY_HAPTIC = "haptic_feedback"
        private const val KEY_FLOATING_OVERLAY = "floating_overlay_enabled"
        private const val KEY_TOTAL_SCROLLS = "total_scrolls_count"
        private const val KEY_PAUSE_ON_TOUCH = "pause_on_touch"
    }

    var selectedPreset: ScrollPreset
        get() {
            val name = prefs.getString(KEY_PRESET, ScrollPreset.ONE_MINUTE.name)
            return try {
                ScrollPreset.valueOf(name ?: ScrollPreset.ONE_MINUTE.name)
            } catch (e: Exception) {
                ScrollPreset.ONE_MINUTE
            }
        }
        set(value) = prefs.edit().putString(KEY_PRESET, value.name).apply()

    var customSeconds: Int
        get() = prefs.getInt(KEY_CUSTOM_SECONDS, 60)
        set(value) = prefs.edit().putInt(KEY_CUSTOM_SECONDS, value.coerceIn(3, 3600)).apply()

    var swipeDirection: SwipeDirection
        get() {
            val name = prefs.getString(KEY_SWIPE_DIRECTION, SwipeDirection.UP.name)
            return try {
                SwipeDirection.valueOf(name ?: SwipeDirection.UP.name)
            } catch (e: Exception) {
                SwipeDirection.UP
            }
        }
        set(value) = prefs.edit().putString(KEY_SWIPE_DIRECTION, value.name).apply()

    var swipeSpeed: SwipeSpeed
        get() {
            val name = prefs.getString(KEY_SWIPE_SPEED, SwipeSpeed.NORMAL.name)
            return try {
                SwipeSpeed.valueOf(name ?: SwipeSpeed.NORMAL.name)
            } catch (e: Exception) {
                SwipeSpeed.NORMAL
            }
        }
        set(value) = prefs.edit().putString(KEY_SWIPE_SPEED, value.name).apply()

    var swipeDistance: SwipeDistance
        get() {
            val name = prefs.getString(KEY_SWIPE_DISTANCE, SwipeDistance.FULL.name)
            return try {
                SwipeDistance.valueOf(name ?: SwipeDistance.FULL.name)
            } catch (e: Exception) {
                SwipeDistance.FULL
            }
        }
        set(value) = prefs.edit().putString(KEY_SWIPE_DISTANCE, value.name).apply()

    var soundChimeEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_CHIME, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_CHIME, value).apply()

    var hapticFeedbackEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTIC, value).apply()

    var floatingOverlayEnabled: Boolean
        get() = prefs.getBoolean(KEY_FLOATING_OVERLAY, true)
        set(value) = prefs.edit().putBoolean(KEY_FLOATING_OVERLAY, value).apply()

    var pauseOnTouch: Boolean
        get() = prefs.getBoolean(KEY_PAUSE_ON_TOUCH, true)
        set(value) = prefs.edit().putBoolean(KEY_PAUSE_ON_TOUCH, value).apply()

    var totalScrollsCount: Int
        get() = prefs.getInt(KEY_TOTAL_SCROLLS, 0)
        set(value) = prefs.edit().putInt(KEY_TOTAL_SCROLLS, value).apply()

    fun incrementScrollCount() {
        totalScrollsCount = totalScrollsCount + 1
    }

    fun getEffectiveDurationSeconds(): Int {
        return if (selectedPreset == ScrollPreset.CUSTOM) {
            customSeconds
        } else {
            selectedPreset.durationSeconds
        }
    }
}
