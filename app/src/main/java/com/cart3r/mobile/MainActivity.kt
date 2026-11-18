package com.cart3r.mobile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val nativeBridge by lazy { NativeBridge() }
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 36, 28, 28)
        }

        val title = TextView(this).apply {
            text = "CART3R MOBILE"
            textSize = 26f
        }
        status = TextView(this).apply {
            textSize = 15f
            setPadding(0, 24, 0, 24)
        }

        val permissionButton = Button(this).apply {
            text = "Разрешение на отображение поверх окон"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                } else {
                    refreshStatus()
                }
            }
        }

        val startButton = Button(this).apply {
            text = "Запустить демонстрационный HUD"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, OverlayService::class.java))
                    refreshStatus()
                } else {
                    status.text = "Сначала предоставь разрешение на отображение поверх окон."
                }
            }
        }

        val stopButton = Button(this).apply {
            text = "Остановить HUD"
            setOnClickListener {
                stopService(Intent(this@MainActivity, OverlayService::class.java))
                refreshStatus()
            }
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(permissionButton)
        layout.addView(startButton)
        layout.addView(stopButton)
        setContentView(layout)
        refreshStatus()
    }

    override fun onResume() {
        super.onResume()
        if (::status.isInitialized) refreshStatus()
    }

    private fun refreshStatus() {
        val nativeLabel = runCatching { nativeBridge.nativeVersion() }
            .getOrDefault("Native module unavailable")
        val overlay = if (Settings.canDrawOverlays(this)) "Overlay permission: granted" else "Overlay permission: required"
        status.text = "$overlay\n${RootChecker.statusText()}\n$nativeLabel\nHUD: demo scene only"
    }
}
