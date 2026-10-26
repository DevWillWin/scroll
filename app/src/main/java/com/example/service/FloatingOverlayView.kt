package com.example.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

@SuppressLint("ViewConstructor")
class FloatingOverlayView(
    context: Context,
    private val windowManager: WindowManager,
    private val layoutParams: WindowManager.LayoutParams
) : FrameLayout(context) {

    private val expandedLayout: LinearLayout
    private val minimizedLayout: FrameLayout
    private val countdownTextView: TextView
    private val playPauseButton: ImageView
    private val miniCountdownTextView: TextView

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var isDragging = false

    init {
        // Build Expanded Layout
        expandedLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8))
            background = GradientDrawable().apply {
                setColor(0xF0101524.toInt()) // Deep slate glass
                cornerRadius = dpToPx(24).toFloat()
                setStroke(dpToPx(1), 0xFF00E5FF.toInt()) // Neon cyan border
            }
            elevation = dpToPx(10).toFloat()
        }

        // Timer badge
        countdownTextView = TextView(context).apply {
            textSize = 15f
            setTextColor(0xFF00E5FF.toInt())
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(dpToPx(6), dpToPx(4), dpToPx(8), dpToPx(4))
            text = "1:00"
        }
        expandedLayout.addView(countdownTextView)

        // Previous button
        val prevButton = createIconButton(android.R.drawable.ic_media_previous, "Prev") {
            AutoScrollController.triggerPreviousScroll()
        }
        expandedLayout.addView(prevButton)

        // Play/Pause button
        playPauseButton = createIconButton(android.R.drawable.ic_media_pause, "Play/Pause") {
            AutoScrollController.togglePlayPause()
        }
        expandedLayout.addView(playPauseButton)

        // Skip / Next button
        val nextButton = createIconButton(android.R.drawable.ic_media_next, "Next") {
            AutoScrollController.triggerImmediateScroll()
        }
        expandedLayout.addView(nextButton)

        // Minimize button
        val minButton = createIconButton(android.R.drawable.arrow_down_float, "Minimize") {
            AutoScrollController.toggleOverlayMinimized()
        }
        expandedLayout.addView(minButton)

        // Close button
        val closeButton = createIconButton(android.R.drawable.ic_menu_close_clear_cancel, "Close") {
            AutoScrollController.stopAutoScroll()
        }
        expandedLayout.addView(closeButton)

        // Build Minimized Layout (Floating round badge)
        minimizedLayout = FrameLayout(context).apply {
            val size = dpToPx(52)
            layoutParams = LayoutParams(size, size)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(0xF2101524.toInt())
                setStroke(dpToPx(2), 0xFFFF2A85.toInt()) // Pink neon ring
            }
            elevation = dpToPx(10).toFloat()
            visibility = View.GONE

            setOnClickListener {
                if (!isDragging) {
                    AutoScrollController.toggleOverlayMinimized()
                }
            }
        }

        miniCountdownTextView = TextView(context).apply {
            textSize = 13f
            setTextColor(0xFFFFFFFF.toInt())
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            text = "60s"
        }
        val miniTextParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        minimizedLayout.addView(miniCountdownTextView, miniTextParams)

        addView(expandedLayout)
        addView(minimizedLayout)

        setupDragListener()
        updateUI(AutoScrollController.state.value)
    }

    private fun createIconButton(resId: Int, desc: String, onClick: () -> Unit): ImageView {
        return ImageView(context).apply {
            setImageResource(resId)
            setColorFilter(Color.WHITE)
            contentDescription = desc
            val pad = dpToPx(8)
            setPadding(pad, pad, pad, pad)
            val btnParams = LinearLayout.LayoutParams(dpToPx(38), dpToPx(38)).apply {
                setMargins(dpToPx(2), 0, dpToPx(2), 0)
            }
            layoutParams = btnParams
            background = GradientDrawable().apply {
                setColor(0x332A3756)
                cornerRadius = dpToPx(19).toFloat()
            }
            setOnClickListener {
                if (!isDragging) {
                    onClick()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragListener() {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - initialTouchX).toInt()
                    val dy = (event.rawY - initialTouchY).toInt()
                    if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                        isDragging = true
                    }
                    if (isDragging) {
                        layoutParams.x = initialX + dx
                        layoutParams.y = initialY + dy
                        try {
                            windowManager.updateViewLayout(this, layoutParams)
                        } catch (e: Exception) {
                            // view may be detached
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        performClick()
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun updateUI(state: AutoScrollState) {
        post {
            val mins = state.secondsRemaining / 60
            val secs = state.secondsRemaining % 60
            val formatted = if (mins > 0) String.format("%d:%02d", mins, secs) else "${secs}s"

            countdownTextView.text = formatted
            miniCountdownTextView.text = formatted

            if (state.isPaused) {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                countdownTextView.setTextColor(0xFFF59E0B.toInt())
            } else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                countdownTextView.setTextColor(0xFF00E5FF.toInt())
            }

            if (state.overlayMinimized) {
                expandedLayout.visibility = View.GONE
                minimizedLayout.visibility = View.VISIBLE
            } else {
                expandedLayout.visibility = View.VISIBLE
                minimizedLayout.visibility = View.GONE
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
