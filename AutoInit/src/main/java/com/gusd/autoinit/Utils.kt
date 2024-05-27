package com.gusd.autoinit

import android.app.ActivityManager
import android.content.Context
import android.os.Process

internal fun getProcessName(context: Context): String {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val myPid = Process.myPid()
    am.runningAppProcesses.forEach {
        if (it.pid == myPid) {
            return it.processName
        }
    }
    return ""
}