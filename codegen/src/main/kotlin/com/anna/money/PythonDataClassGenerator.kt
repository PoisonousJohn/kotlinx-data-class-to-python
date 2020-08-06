//package com.anna.money
//
//import java.io.File
//import kotlin.reflect.KClass
//
//class PythonDataClassGenerator(
//    private val outputPath: String,
//    private val packageContext: PackageContext = PackageContext(),
//    private val typeConverters: List<TypeConverter> = FileContext.defaultTypeConverters
//) {
//
//
//    fun generateFiles(input: List<KClass<*>>) {
//        for (clazz in input) {
//            if (!clazz.isSerializable())
//                throw java.lang.IllegalArgumentException("Class $clazz should be Serializable")
//
//            packageContext.moduleFilePaths.addAll(getModuleFilesToGenerate(clazz))
//            val fileContext = clazz.toFileContext(typeConverters = typeConverters)
//
//            if (packageContext.generatedTypes.contains(fileContext)) continue
//
//            val sourceCode = fileContext.generateSourceCode()
//            File(fileContext.path).apply {
//                parentFile.mkdirs()
//                createNewFile()
//                writeText(sourceCode)
//            }
//            packageContext.generatedTypes.add(fileContext)
//            generateFiles(fileContext.nestedClasses)
//        }
//        for (moduleFilePath in packageContext.moduleFilePaths) {
//            File(moduleFilePath).createNewFile()
//        }
//    }
//
//    fun generateSourceCode(clazz: KClass<*>) = clazz.toFileContext(typeConverters).generateSourceCode()
//
//    fun getModuleFilesToGenerate(clazz: KClass<*>): List<String> {
//        val output = mutableListOf<String>()
//        val basePathDir = File(outputPath)
//        var parentDir = File(getFilePath(clazz)).parentFile
//        while (parentDir.absolutePath != basePathDir.absolutePath) {
//            output.add("${parentDir.absolutePath}/__init__.py")
//            parentDir = parentDir.parentFile
//        }
//        return output
//    }
//
//    fun getFilePath(clazz: KClass<*>) =
//        "$outputPath/${clazz.qualifiedName!!.split(".")
//            .joinToString("/", transform = ::getSnakeCaseName)}.py"
//
//    private fun <T : Any> KClass<T>.toFileContext(typeConverters: List<TypeConverter>) =
//        FileContext(
//            clazz = this,
//            path = getFilePath(this),
//            nestedClasses = getNestedSerializableClasses(),
//            typeConverters = typeConverters
//        )
//
//    data class PackageContext(
//        val moduleFilePaths: MutableList<String> = mutableListOf(),
//        val generatedTypes: MutableSet<FileContext> = mutableSetOf()
//    )
//}