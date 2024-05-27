package com.gusd.common

import android.content.Context
import android.util.Log
import com.gusd.annotation.AutoInit
import com.gusd.autoinit.IAutoInitializer

@AutoInit
class Initializer0 : IAutoInitializer {
    override fun onInit(context: Context, data: Map<String, Any>) {
        Log.d("AutoInit", "${getName()}.onInit from common")
    }

    override fun getName(): String {
        return "commonInit"
    }

    override fun getPriority(): Int {
        return Int.MIN_VALUE
    }
}