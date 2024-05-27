package com.gusd.autoinit

import android.content.Context
import com.gusd.annotation.AutoInit
import java.util.ServiceLoader

object AutoInitManager {
    private var initLists: ServiceLoader<IAutoInitializer>

    init {
        val startTime = System.currentTimeMillis()
        initLists = ServiceLoader.load(IAutoInitializer::class.java)
        Logger.d("load initializer list took ${System.currentTimeMillis() - startTime}ms")
    }

    fun init(context: Context, data: Map<String, Any>) {
        val processName = getProcessName(context)
        val shouldInitList = initLists.filter { it.shouldInit(context, processName) }
        // init by priority
        shouldInitList.sortedBy { it.getPriority() }.forEach {
            it.onInit(context, data)
        }
    }


    fun unInit() {
    }


}