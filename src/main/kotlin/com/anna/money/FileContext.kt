package com.anna.money

import kotlinx.serialization.SerialName
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty

data class FileContext(
    val clazz: KClass<*>,
    val path: String,
    val nestedClasses: List<KClass<*>>,
    val typeMappings: Map<KClassifier, PythonTypeMapping> = defaultTypeMappings
) {
    private val output = StringBuilder()
    private val customMappingsWithImports = mutableListOf<PythonTypeMapping>()

    fun generateSourceCode(): String {
        output.clear()

        output.append(getDefaultImports())
        for (pythonImport in getCustomImports()) {
            if (pythonImport.import.isNotBlank())
                output.append("${pythonImport.import}\n")
        }
        for (importString in nestedClasses.map { it.toImportString() }) {
            output.append("$importString\n")
        }
        output.append(getClassHeader(clazz.simpleName!!))
        output.append(generateProperties())
        output.append("\n\n")
        generateValidation(clazz)
        output.append("\n")

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
        clazz.members.filterIsInstance<KProperty<*>>()
            .map { it.getter.returnType.classifier }
            .mapNotNull { typeMappings[it] }

    private fun generateProperties() =
        StringBuilder().apply {
            withIndentLevel(1) {
                val properties = clazz.members.filterIsInstance<KProperty<*>>()
                for (property in properties) {
                    val propertyType =
                        if (typeMappings.containsKey(property.getter.returnType.classifier)) {
                            val mapping = typeMappings[property.getter.returnType.classifier]!!
                            customMappingsWithImports.add(mapping)
                            mapping.typeName
                        } else {
                            property.toPythonType()
                        }
                    append("${property.serializableName()}: $propertyType")
                }
            }
        }.toString()

    private fun KProperty<*>.toPythonType() = getter.returnType.classifier!!.let { propertyType ->
        val kClass = propertyType as? KClass<*>
        // support nested serializable classes
        if (kClass.isSerializable()) return kClass?.simpleName!!
        typeMappings[propertyType]?.typeName
            ?: throw IllegalArgumentException("Type $this is not supported")
    }

    private fun <T : Any> generateValidation(clazz: KClass<T>) {
        withIndentLevel(1) {
            append("def validate(self):\n")
        }
        withIndentLevel(2) {
            val properties = clazz.members.filterIsInstance<KProperty<*>>()
            for (property in properties) {
                append("if not isinstance(self.${property.serializableName()}, ${property.toPythonType()}):\n")
                withIndentLevel(3) {
                    append("raise RuntimeError(f'${clazz.simpleName}.${property.serializableName()} value {self.${property.serializableName()}} should be of type ${property.toPythonType()}')")
                }
            }
        }
    }

    companion object {
        val defaultTypeMappings = mapOf<KClassifier, PythonTypeMapping>(
            Int::class to PythonTypeMapping(typeName = "int"),
            String::class to PythonTypeMapping(typeName = "str"),
            Float::class to PythonTypeMapping(typeName = "float"),
            Boolean::class to PythonTypeMapping(typeName = "bool")
        )
    }
}

data class PythonTypeMapping(
    val typeName: String,
    val import: String = ""
)
