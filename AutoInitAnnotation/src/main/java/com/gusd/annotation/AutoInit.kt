package com.gusd.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoInit(val processNames: Array<String> = [], val name: String = "")