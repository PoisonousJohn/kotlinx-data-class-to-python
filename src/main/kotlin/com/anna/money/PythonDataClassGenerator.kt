package com.anna.money

import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class PythonDataClassGenerator(
    private val basePath: String,
    private val packageContext: PackageContext = PackageContext(),
    private val typeMappings: Map<KClassifier, PythonTypeMapping> = FileContext.defaultTypeMappings
) {


    fun generateFiles(input: List<KClass<*>>) {
        for (clazz in input) {
            if (!clazz.isSerializable())
                throw java.lang.IllegalArgumentException("Class $clazz should be Serializable")

            packageContext.moduleFilePaths.addAll(getModuleFilesToGenerate(clazz))
            val fileContext = clazz.toFileContext(typeMappings = typeMappings)

            if (packageContext.generatedTypes.contains(fileContext)) continue

            val sourceCode = fileContext.generateSourceCode()
            File(fileContext.path).apply {
                parentFile.mkdirs()
                createNewFile()
                writeText(sourceCode)
            }
            packageContext.generatedTypes.add(fileContext)
            generateFiles(fileContext.nestedClasses)
        }
        for (moduleFilePath in packageContext.moduleFilePaths) {
            File(moduleFilePath).createNewFile()
        }
    }

    fun generateSourceCode(clazz: KClass<*>) = clazz.toFileContext(typeMappings).generateSourceCode()

    fun getModuleFilesToGenerate(clazz: KClass<*>): List<String> {
        val output = mutableListOf<String>()
        val basePathDir = File(basePath)
        var parentDir = File(getFilePath(clazz)).parentFile
        while (parentDir.absolutePath != basePathDir.absolutePath) {
            output.add("${parentDir.absolutePath}/__init__.py")
            parentDir = parentDir.parentFile
        }
        return output
    }

    fun getFilePath(clazz: KClass<*>) =
        "$basePath/${clazz.qualifiedName!!.split(".")
            .joinToString("/", transform = ::getSnakeCaseName)}.py"

    private fun <T : Any> KClass<T>.toFileContext(typeMappings: Map<KClassifier, PythonTypeMapping>) =
        FileContext(
            clazz = this,
            path = getFilePath(this),
            nestedClasses = getNestedSerializableClasses(),
            typeMappings = typeMappings
        )

    data class PackageContext(
        val moduleFilePaths: MutableList<String> = mutableListOf(),
        val generatedTypes: MutableSet<FileContext> = mutableSetOf()
    )
}