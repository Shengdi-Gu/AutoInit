package com.gusd.autoinit

import android.util.Log

private const val TAG = "AutoInit"

object Logger {

    var level = Log.VERBOSE
    fun d(message: String) {
        if (level <= Log.DEBUG) {
            Log.d(TAG, message)
        }
    }

    fun e(message: String) {
        if (level <= Log.ERROR) {
            Log.e(TAG, message)
        }
    }

    fun i(message: String) {
        if (level <= Log.INFO) {
            Log.i(TAG, message)
        }
    }

    fun w(message: String) {
        if (level <= Log.WARN) {
            Log.w(TAG, message)
        }
    }

    fun v(message: String) {
        if (level <= Log.VERBOSE) {
            Log.v(TAG, message)
        }
    }

    fun wtf(message: String) {
        if (level <= Log.ASSERT) {
            Log.wtf(TAG, message)
        }
    }
}