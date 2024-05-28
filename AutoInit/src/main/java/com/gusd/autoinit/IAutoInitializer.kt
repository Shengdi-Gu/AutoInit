package com.gusd.autoinit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.gusd.annotation.AutoInit

interface IAutoInitializer {

    fun onInit(context: Context, intent: Intent? = null)

    fun getName(): String {
        val name = this.javaClass.getAnnotation(AutoInit::class.java)?.name
        return if (name.isNullOrEmpty()) {
            this.javaClass.simpleName
        } else {
            name
        }
    }

    /**
     * 数值越小，优先级越高
     * 当数值小于 0 时，当前 IAutoInitializer 只能在主线程执行，不支持异步初始化，通常用于基础库的初始化
     * 当数值大于 0 时，当前 IAutoInitializer 支持同步和异步初始化
     */
    fun getPriority(): Int

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

    fun onActivitySaveInstanceState(activity: Activity) {

    }

    fun onProcessBackground() {

    }

    fun onProcessForeground() {

    }

    fun onTerminate() {

    }

    fun shouldInit(context: Context, processName: String): Boolean {
        this::class.java.getAnnotation(AutoInit::class.java)?.let {
            return it.processNames.contains(processName)
                    || it.processNames.contains("*")
                    || (it.processNames.isEmpty() && processName == context.packageName)
        }
        return false
    }

    fun onConfigurationChanged(newConfig: Configuration) {

    }

    fun onLowMemory() {

    }
}