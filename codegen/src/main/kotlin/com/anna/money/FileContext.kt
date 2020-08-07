package com.anna.money

import com.anna.money.PythonLangHelpers.Companion.indentBy
import com.google.common.annotations.VisibleForTesting
import kotlinx.serialization.SerialName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.KProperty

data class FileContext(
    val classElement: TypeElement,
    val typeConverters: List<TypeConverter> = defaultTypeConverters
) {
    private val output = StringBuilder()

    fun generateSourceCode(): String {
        output.clear()

        output.append(getDefaultImports())
        for (pythonImport in getCustomImports()) {
            output.append(pythonImport.toImportString())
        }
        output.append(getClassHeader(classElement.simpleName.toString()))
        output.append(generateProperties())
        output.append("\n")
        generateValidation()

        return output.toString()
    }

    private fun getClassHeader(className: String) =
        "@dataclass\nclass $className:\n"

    private fun getDefaultImports() =
        "from dataclasses import dataclass\n\n"

    private fun withIndentLevel(level: Int, builder: StringBuilder.() -> Unit) {
        output.append("    ".repeat(level))
        builder.invoke(output)
    }

    private fun KProperty<*>.serializableName() =
        annotations.filterIsInstance<SerialName>().firstOrNull()?.value ?: name

    private fun getCustomImports() =
        classElement.getFields()
            .flatMap { field ->
                getTypeConverter(field).getCustomImports(field)
            }

    private fun getPythonTypeDeclaration(property: Element) =
        getTypeConverter(property).getPythonTypeDeclaration(property)

    private fun generateProperties() =
        StringBuilder().apply {
            for (property in classElement.getFields()) {
                try {
                    withIndentLevel(1) {
                        append("${property.getSerializedName()}: ${getPythonTypeDeclaration(property)}\n")
                    }
                } catch (throwable: Throwable) {
                    print("Failed to generate property ${classElement.asType()}.${property.simpleName} of type ${property.asType()} with exc: $throwable")
                    throw throwable
                }
            }
        }.toString()

    private fun generateValidation() {
        withIndentLevel(1) {
            append("def validate(self):\n")
        }
        val properties = classElement.getFields()
        for (property in properties) {
            val validationString = getTypeConverter(property)
                .getValidationString("self.${property.getSerializedName()}", property)
            output.append(
                validationString.indentBy(2)
            )
            output.append("\n")
        }
    }

    @VisibleForTesting
    private fun packageToModuleDirs(pkg: String): Set<String> {
        fun Iterable<String>.toModuleDir() = joinToString("/")
        val output = mutableSetOf<String>()
        val packageComponents = pkg.split(".").toMutableList()
        // usually, final python package's part points to the file, and needs no directory
        // thus we should remove it
        packageComponents.removeAt(packageComponents.size - 1)
        while (packageComponents.isNotEmpty()) {
            output.add(packageComponents.toModuleDir())
            packageComponents.removeAt(packageComponents.size - 1)
        }
        return output
    }

    fun getModuleDirs() =
        packageToModuleDirs(classElement.getPackage()).toMutableSet().also { moduleFiles ->
            getTypeConverter(classElement).getCustomImports(classElement)
                .map {
                    packageToModuleDirs(it.packageName)
                }.forEach {
                    moduleFiles.addAll(it)
                }
        }

    private fun getTypeConverter(type: Element) =
        typeConverters.firstOrNull { it.isTypeSupported(type) }
            ?: throw IllegalArgumentException("Type $type is not supported")

    companion object {
        val defaultTypeConverters = listOf(
            PrimitiveTypesConverter(),
            SerializableTypeConverter(),
            GenericListTypeConverter()
        )
    }
}
