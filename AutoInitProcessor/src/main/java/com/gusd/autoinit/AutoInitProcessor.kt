package com.gusd.autoinit

import com.gusd.annotation.AutoInit
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


private const val INITIALIZER_INTERFACE = "com.gusd.autoinit.IAutoInitializer"

@SupportedOptions(value = ["debug", "verify"])
class AutoInitProcessor : AbstractProcessor() {
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        log("Processing annotations")
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AutoInit::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }


    private fun log(msg: String) {
        if (processingEnv.options.containsKey("debug")) {
            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, msg)
        }
    }

    private fun warning(msg: String, element: Element, annotation: AnnotationMirror) {
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, msg, element, annotation)
    }

    private fun error(msg: String, element: Element, annotation: AnnotationMirror) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg, element, annotation)
    }

    private fun fatalError(msg: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: $msg")
    }
}