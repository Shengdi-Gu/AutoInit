package com.gusd.autoinit

import android.app.Activity
import android.content.Context
import com.gusd.annotation.AutoInit

interface IAutoInitializer {

    fun onInit(context: Context, data: Map<String, Any> = emptyMap())

    fun getName(): String {
        val name = this.javaClass.getAnnotation(AutoInit::class.java)?.name
        return if (name.isNullOrEmpty()) {
            this.javaClass.simpleName
        } else {
            name
        }

    }

    fun unInit() {

    }

    fun onActivityCreate(activity: Activity) {

    }

    fun onActivityDestroy(activity: Activity) {

    }

    fun onActivityPause(activity: Activity) {

    }

    fun onActivityResume(activity: Activity) {

    }

    fun onActivityStart(activity: Activity) {

    }

    fun onActivityStop(activity: Activity) {

    }

    fun onActivityRestart(activity: Activity) {

    }

    fun onActivitySaveInstanceState(activity: Activity) {

    }

    fun onActivityRestoreInstanceState(activity: Activity) {

    }

    fun onProcessBackground() {

    }

    fun onProcessForeground() {

    }

    fun onTerminate() {

    }
}