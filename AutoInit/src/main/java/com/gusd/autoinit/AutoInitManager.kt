package com.gusd.autoinit

import android.content.Context
import java.util.ServiceLoader

object AutoInitManager {
    private var initLists: ServiceLoader<IAutoInitializer>

    init {
        val startTime = System.currentTimeMillis()
        initLists = ServiceLoader.load(IAutoInitializer::class.java)
        Logger.d("load initializer list took ${System.currentTimeMillis() - startTime}ms")
    }

    fun init(context: Context, data: Map<String, Any>) {
        initLists.forEach {
            it.onInit(context.applicationContext)
        }
    }


    fun unInit() {
        initLists.forEach {
            it.unInit()
        }
    }


}