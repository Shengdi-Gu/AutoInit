package com.gusd.autoinit

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.util.ServiceLoader

object AutoInitManager : Application.ActivityLifecycleCallbacks, LifecycleEventObserver,
    ComponentCallbacks {
    private var initLists: ServiceLoader<IAutoInitializer>

    private var initList: MutableList<IAutoInitializer> = mutableListOf()

    private lateinit var application: Application

    init {
        val startTime = System.currentTimeMillis()
        initLists = ServiceLoader.load(IAutoInitializer::class.java)
        Logger.d("load initializer list took ${System.currentTimeMillis() - startTime}ms")
    }

    fun init(context: Context, intent: Intent? = null) {
        application = context.applicationContext as Application
        val processName = getProcessName(context)
        val shouldInitList =
            initLists.filter { it.shouldInit(context, it::class.java, processName) }
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerComponentCallbacks(this)
        // init by priority
        shouldInitList.sortedBy { it.getPriority() }.forEach {
            it.onInit(context, intent)
            initList.add(it)
        }

    }


    fun unInit() {
        initList.forEach {
            it.unInit()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        initList.forEach {
            it.onActivityCreate(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        initList.forEach {
            it.onActivityStart(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        initList.forEach {
            it.onActivityResume(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        initList.forEach {
            it.onActivityPause(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        initList.forEach {
            it.onActivityStop(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        initList.forEach {
            it.onActivitySaveInstanceState(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        initList.forEach {
            it.onActivityDestroy(activity)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        initList.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }

    override fun onLowMemory() {
        initList.forEach {
            it.onLowMemory()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                initList.forEach {
                    it.onProcessForeground()
                }
            }

            Lifecycle.Event.ON_STOP -> {
                initList.forEach {
                    it.onProcessBackground()
                }
            }

            else -> {
            }
        }
    }


}