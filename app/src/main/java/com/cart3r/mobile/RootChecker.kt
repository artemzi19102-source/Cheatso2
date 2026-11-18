package com.cart3r.mobile

import java.io.File

object RootChecker {
    fun isRootLikelyAvailable(): Boolean {
        val paths = listOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/data/adb/magisk"
        )
        return paths.any { File(it).exists() }
    }

    fun statusText(): String =
        if (isRootLikelyAvailable()) "Root: possible (not verified)"
        else "Root: not detected"
}
