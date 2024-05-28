package com.gusd.module3

import android.content.Context
import android.content.Intent
import android.util.Log
import com.gusd.annotation.AutoInit
import com.gusd.autoinit.IAutoInitializer

@AutoInit
class Initializer5 : IAutoInitializer {
    override fun onInit(context: Context, intent: Intent?) {
        Log.d("AutoInit", "${getName()}.onInit from module3")
    }

    override fun getPriority(): Int {
        return 5
    }
}