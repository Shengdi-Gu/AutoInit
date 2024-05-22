package com.gusd.autoinit

import com.gusd.annotation.AutoInit
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation


private const val INITIALIZER_INTERFACE = "com.gusd.autoinit.IAutoInitializer"

@SupportedOptions(value = ["debug", "verify"])
class AutoInitProcessor : AbstractProcessor() {

    private val providers = hashSetOf<String>()
    override fun process(
        annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment
    ): Boolean {
        log("Processing annotations")
        try {
            processImpl(roundEnv)
        } catch (e: Exception) {
            val stackTrace = e.stackTrace.joinToString("\n")
            fatalError("Exception thrown: ${e.message}\n$stackTrace")
        }
        return false
    }

    private fun processImpl(roundEnv: RoundEnvironment) {
        if (roundEnv.processingOver()) {
            generateConfigFiles()
        } else {
            processAnnotations(roundEnv)
        }
    }

    private fun processAnnotations(
        roundEnv: RoundEnvironment
    ) {
        val elements = roundEnv.getElementsAnnotatedWith(AutoInit::class.java)
        elements.forEach { element ->
            val annotation =
                element.annotationMirrors.find { it.annotationType.toString() == AutoInit::class.java.name }
            if (annotation != null && element != null) {
                processElement(element, annotation)
            }
        }
    }

    private fun processElement(element: Element, annotation: AnnotationMirror) {
        if (element !is TypeElement) {
            error("Element is not a TypeElement", element, annotation)
            return
        }
        val className = getClassNameFromElement(element)
        checkImplementInterface(element, annotation)
        checkConstructorWithEmptyParams(element, annotation)
        checkNotAbstract(element, annotation)
        log("Found provider: $className")
        providers.add(className)
    }

    private fun getClassNameFromElement(element: TypeElement): String {
        val qualifiedName = element.qualifiedName.toString()
        val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()
        val classNameWithoutPackage = qualifiedName.substring(packageName.length + 1)
        return "$packageName.${classNameWithoutPackage.replace('.', '$')}"
    }


    private fun checkNotAbstract(element: TypeElement, annotation: AnnotationMirror) {
        if (element.modifiers.contains(javax.lang.model.element.Modifier.ABSTRACT)) {
            error("Class ${getClassNameFromElement(element)} is abstract", element, annotation)
        }
    }

    private fun checkConstructorWithEmptyParams(
        element: TypeElement, annotation: AnnotationMirror
    ) {
        val constructors =
            element.enclosedElements.filterIsInstance<javax.lang.model.element.ExecutableElement>()
                .filter { it.kind == javax.lang.model.element.ElementKind.CONSTRUCTOR }
        val defaultConstructor = constructors.any { it.parameters.isEmpty() }
        if (!defaultConstructor) {
            error(
                "Requires an empty constructor ${getClassNameFromElement(element)}",
                element,
                annotation
            )
        }
    }

    private fun checkImplementInterface(element: TypeElement, annotation: AnnotationMirror) {
        val interfaces = element.interfaces
        if (interfaces.isEmpty()) {
            error(
                "Class ${getClassNameFromElement(element)} does not implement $INITIALIZER_INTERFACE",
                element,
                annotation
            )
        } else {
            val found = interfaces.any { it.toString() == INITIALIZER_INTERFACE }
            if (!found) {
                error(
                    "Class ${getClassNameFromElement(element)} does not implement $INITIALIZER_INTERFACE",
                    element,
                    annotation
                )
            }
        }


    }

    private fun generateConfigFiles() {
        log("Generating config files")
        val filer = processingEnv.filer
        val path: String = ServicesFiles.getPath(INITIALIZER_INTERFACE)
        val existing = try {
            val file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", path)
            ServicesFiles.readServiceFile(file.openInputStream())
        } catch (e: Exception) {
            emptySet()
        }
        val allProviders = existing + providers
        val writer =
            filer.createResource(StandardLocation.CLASS_OUTPUT, "", path).openOutputStream()
        ServicesFiles.writeServiceFile(allProviders, writer)
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