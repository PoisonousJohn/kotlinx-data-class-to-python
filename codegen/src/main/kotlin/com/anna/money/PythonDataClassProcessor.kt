package com.anna.money

import com.google.auto.service.AutoService
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
//@SupportedOptions("kapt.kotlin.generated")
class PythonDataClassProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PythonDataClass::class.java.name)
    }

//    override fun getSupportedSourceVersion(): SourceVersion {
//        return SourceVersion.latest()
//    }

    private fun processClass(classElement: TypeElement) {
        try {
            val sourceCode =
                FileContext(classElement = classElement, path = "", nestedClasses = emptyList())
                    .generateSourceCode()
            processingEnv.messager.printMessage(
                Diagnostic.Kind.WARNING,
                "${sourceCode}\n"
            )
        } catch (throwable: Throwable) {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "${stringWriter}\n"
            )
        }
//        val output = classElement.annotationMirrors.any {
//            it.annotationType.toString() == "kotlinx.serialization.Serializable"
//        }
//        processingEnv.messager.printMessage(
//            Diagnostic.Kind.NOTE,
//            "${output}\n"
//        )
//        return
//        for (field in classElement.getFields()) {
////                    val output = (enclosed.asType() as DeclaredType).asElement().toString()
//            processingEnv.messager.printMessage(
//                Diagnostic.Kind.WARNING,
//                "${output}\n"
//            )
//        }
    }

    override fun process(p0: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment): Boolean {
        try {
            for (element in roundEnvironment.getElementsAnnotatedWith(PythonDataClass::class.java)) {
                processClass(element as TypeElement)
            }
        } catch (throwable: Throwable) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, throwable.toString())
        }
        return true
    }
}