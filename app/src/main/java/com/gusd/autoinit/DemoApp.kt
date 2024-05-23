package com.gusd.autoinit

import android.app.Application

class DemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AutoInitManager.init(this)
    }
}