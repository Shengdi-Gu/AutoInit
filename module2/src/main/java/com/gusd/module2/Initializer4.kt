package com.gusd.module2

import android.content.Context
import android.util.Log
import com.gusd.annotation.AutoInit
import com.gusd.autoinit.IAutoInitializer

@AutoInit
class Initializer4 : IAutoInitializer {
    override fun onInit(context: Context, data: Map<String, Any>) {
        Log.d("AutoInit", "${getName()}.onInit from module2")
    }

    override fun getPriority(): Int {
        return 4
    }
}