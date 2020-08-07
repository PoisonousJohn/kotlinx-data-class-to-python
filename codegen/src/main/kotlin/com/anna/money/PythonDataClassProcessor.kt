package com.anna.money

import com.google.auto.service.AutoService
import java.io.PrintWriter
import java.io.StringWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
//@SupportedOptions("kapt.kotlin.generated")
class PythonDataClassProcessor : AbstractProcessor() {
    private val moduleDirs = mutableSetOf<String>()

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PythonDataClass::class.java.name)
    }

//    override fun getSupportedSourceVersion(): SourceVersion {
//        return SourceVersion.latest()
//    }

    private fun processClass(classElement: TypeElement) {
        try {
            val fileContext = FileContext(classElement = classElement)
            val sourceCode = fileContext.generateSourceCode()
            val fileName = getSnakeCaseName(classElement.simpleName.toString())
            val classFile = processingEnv.filer.createResource(StandardLocation.SOURCE_OUTPUT, classElement.getPackage(), "${fileName}.py")
            classFile.openWriter().apply {
                append(sourceCode)
                close()
            }
            moduleDirs.addAll(fileContext.getModuleDirs())
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
    private fun createInitFile(`package`: String) {
        try {
            processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                `package`,
                "__init__.py"
            ).openWriter().apply {
                write("# This is a generated file")
                close()
            }
        } catch (throwable: FilerException) {
            // file exists, ignoring
        }
    }

    override fun process(p0: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment): Boolean {
        try {
            for (element in roundEnvironment.getElementsAnnotatedWith(PythonDataClass::class.java)) {
                processClass(element as TypeElement)
            }
            processingEnv.messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, moduleDirs.toString())
            for (moduleDir in moduleDirs) {
                createInitFile(moduleDir.replace("/", "."))
            }
            createInitFile("")
        } catch (throwable: Throwable) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, throwable.toString())
        }
        return true
    }
}