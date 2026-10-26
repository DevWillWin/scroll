package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.example.model.SwipeDirection

class AutoScrollAccessibilityService : AccessibilityService() {

    private var windowManager: WindowManager? = null
    private var floatingOverlayView: FloatingOverlayView? = null
    private var overlayLayoutParams: WindowManager.LayoutParams? = null

    companion object {
        var instance: AutoScrollAccessibilityService? = null
            private set

        fun isServiceRunning(): Boolean = instance != null

        fun isAccessibilitySettingsEnabled(context: Context): Boolean {
            val serviceName = "${context.packageName}/${AutoScrollAccessibilityService::class.java.canonicalName}"
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            return enabledServices.split(":").any {
                it.equals(serviceName, ignoreCase = true) ||
                    it.contains(AutoScrollAccessibilityService::class.java.simpleName, ignoreCase = true)
            }
        }

        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        windowManager = getSystemService(WINDOW_SERVICE) as? WindowManager
        AutoScrollController.setServiceConnected(true)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Can optionally observe active package name for auto-pause or profile switching
    }

    override fun onInterrupt() {
        // Accessibility service interrupted
    }

    override fun onDestroy() {
        hideFloatingOverlay()
        instance = null
        AutoScrollController.setServiceConnected(false)
        super.onDestroy()
    }

    fun performSwipe(
        direction: SwipeDirection,
        distanceFraction: Float,
        durationMs: Long,
        onCompleted: (() -> Unit)? = null
    ) {
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels.toFloat()
        val height = displayMetrics.heightPixels.toFloat()

        val path = Path()

        val startX: Float
        val startY: Float
        val endX: Float
        val endY: Float

        when (direction) {
            SwipeDirection.UP -> {
                // Next Reel: standard swipe up from bottom-center to upper-center
                startX = width * 0.5f
                startY = height * 0.75f
                endX = width * 0.5f
                endY = (height * 0.75f) - (height * distanceFraction)
            }
            SwipeDirection.DOWN -> {
                // Previous Reel: swipe down from top-center to lower-center
                startX = width * 0.5f
                startY = height * 0.25f
                endX = width * 0.5f
                endY = (height * 0.25f) + (height * distanceFraction)
            }
            SwipeDirection.LEFT -> {
                startX = width * 0.85f
                startY = height * 0.5f
                endX = width * 0.15f
                endY = height * 0.5f
            }
            SwipeDirection.RIGHT -> {
                startX = width * 0.15f
                startY = height * 0.5f
                endX = width * 0.85f
                endY = height * 0.5f
            }
        }

        path.moveTo(startX, startY)
        path.lineTo(endX, endY)

        val stroke = GestureDescription.StrokeDescription(path, 0L, durationMs.coerceAtLeast(100L))
        val gesture = GestureDescription.Builder().addStroke(stroke).build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                onCompleted?.invoke()
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    fun showFloatingOverlay() {
        if (floatingOverlayView != null) {
            updateOverlayUI()
            return
        }

        val wm = windowManager ?: return

        val overlayType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = (resources.displayMetrics.widthPixels * 0.15f).toInt()
            y = (resources.displayMetrics.heightPixels * 0.70f).toInt()
        }

        overlayLayoutParams = params
        val view = FloatingOverlayView(this, wm, params)
        floatingOverlayView = view

        try {
            wm.addView(view, params)
        } catch (e: Exception) {
            // Error adding overlay
            floatingOverlayView = null
        }
    }

    fun updateOverlayUI() {
        floatingOverlayView?.updateUI(AutoScrollController.state.value)
    }

    fun hideFloatingOverlay() {
        floatingOverlayView?.let { view ->
            try {
                windowManager?.removeView(view)
            } catch (e: Exception) {
                // view might not be attached
            }
            floatingOverlayView = null
        }
    }
}
