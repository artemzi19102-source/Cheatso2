package com.cart3r.mobile

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private var overlayView: DemoHudView? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val view = DemoHudView(this)
        overlayView = view
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }
        windowManager.addView(view, params)
    }

    override fun onDestroy() {
        overlayView?.let {
            runCatching { windowManager.removeView(it) }
        }
        overlayView = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private class DemoHudView(context: Context) : View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.GREEN
            strokeWidth = 3f
            textSize = 30f
            style = Paint.Style.STROKE
        }
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.GREEN
            textSize = 27f
            style = Paint.Style.FILL
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val w = width.toFloat()
            val h = height.toFloat()

            textPaint.textSize = 30f
            canvas.drawText("CART3R HUD  •  DEMO", 28f, 55f, textPaint)
            textPaint.textSize = 24f
            canvas.drawText("FPS: ${refreshRateEstimate()}  |  ROOT: ${if (RootChecker.isRootLikelyAvailable()) "CHECK" else "NO"}", 28f, 90f, textPaint)

            // Synthetic targets belong only to this standalone demonstration overlay.
            val boxes = listOf(
                Box(w * 0.22f, h * 0.30f, "DEMO-01", "12.4 m"),
                Box(w * 0.62f, h * 0.48f, "DEMO-02", "23.1 m")
            )
            boxes.forEach { item ->
                val left = item.x
                val top = item.y
                val right = left + 150f
                val bottom = top + 230f
                canvas.drawRect(left, top, right, bottom, paint)
                canvas.drawText(item.name, left, top - 12f, textPaint)
                canvas.drawText(item.distance, left, bottom + 30f, textPaint)
            }
            postInvalidateDelayed(1000)
        }

        private fun refreshRateEstimate(): Int = 60

        private data class Box(val x: Float, val y: Float, val name: String, val distance: String)
    }
}
