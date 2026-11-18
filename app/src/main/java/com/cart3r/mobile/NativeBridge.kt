package com.cart3r.mobile

class NativeBridge {
    external fun nativeVersion(): String

    companion object {
        init {
            System.loadLibrary("cart3r")
        }
    }
}
