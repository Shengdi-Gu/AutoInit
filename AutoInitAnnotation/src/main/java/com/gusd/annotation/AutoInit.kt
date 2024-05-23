package com.gusd.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class AutoInit(val processNames: Array<String> = [], val name: String = "")