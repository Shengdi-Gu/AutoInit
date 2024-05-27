package com.gusd.module3

import android.content.Context
import android.util.Log
import com.gusd.annotation.AutoInit
import com.gusd.autoinit.IAutoInitializer

@AutoInit
class Initializer6 : IAutoInitializer {
    override fun onInit(context: Context, data: Map<String, Any>) {
        Log.d("AutoInit", "${getName()}.onInit from module3")
    }

    override fun getPriority(): Int {
        return 6
    }
}