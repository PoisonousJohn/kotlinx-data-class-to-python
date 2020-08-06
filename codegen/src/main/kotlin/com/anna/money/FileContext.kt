package com.anna.money

import kotlinx.serialization.SerialName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.KProperty

data class PythonImport(
    val packageName: String,
    val typeName: String
)

data class FileContext(
    val classElement: TypeElement,
    val path: String,
    val nestedClasses: List<TypeElement>,
    val typeConverters: List<TypeConverter> = defaultTypeConverters
) {
    private val output = StringBuilder()

    fun generateSourceCode(): String {
        output.clear()

        output.append(getDefaultImports())
        for (pythonImport in getCustomImports()) {
            output.append("from ${pythonImport.packageName} import ${pythonImport.typeName}\n")
        }
        output.append(getClassHeader(classElement.simpleName.toString()))
        output.append(generateProperties())
        output.append("\n")
//        generateValidation()

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
                typeConverters
                    .filter { it.isTypeSupported(field) }
                    .flatMap { it.getCustomImports(field) }
            }

    private fun getPythonTypeDeclaration(property: Element) =
        typeConverters.firstOrNull {
            it.isTypeSupported(property)
        }?.getPythonTypeDeclaration(property)
            ?: throw IllegalArgumentException("Property ${property.simpleName} with type ${property.asType()} is not supported")

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
            withIndentLevel(2) {
                append(
                    "if not isinstance(self.${property.getSerializedName()}, ${getPythonTypeDeclaration(
                        property
                    )}):\n"
                )
            }
            withIndentLevel(3) {
                append(
                    "raise RuntimeError(f'${classElement.simpleName}.${property.getSerializedName()} value {self.${property.getSerializedName()}} should be of type ${getPythonTypeDeclaration(
                        property
                    )}')\n"
                )
            }
        }
    }

    companion object {
        val defaultTypeConverters = listOf(
            PrimitiveTypesConverter(),
            SerializableTypeConverter(),
            GenericListTypeConverter()
        )
    }
}

data class PythonTypeMapping(
    val typeName: String,
    val import: String = ""
)
