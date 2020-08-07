package com.anna.money

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind

class PrimitiveTypesConverter : TypeConverter {
    override fun isTypeSupported(type: Element): Boolean =
        when {
            type.asType().toString() == stringQualifier -> true
            defaultTypeMappings.containsKey(type.asType().kind) -> true
            else -> false
        }

    override fun getValidationString(valueGetter: String, type: Element) =
        PythonLangHelpers.validateType(valueGetter, getPythonTypeDeclaration(type))

    override fun getPythonTypeDeclaration(type: Element): String {
        if (type.asType().toString() == stringQualifier) return "str"
        return defaultTypeMappings[type.asType().kind] ?: throw IllegalArgumentException("${type.asType()} is not supported")
    }

    override fun getCustomImports(type: Element): List<PythonImport> = emptyList()

    companion object {
        val stringQualifier: String = String::class.java.name
        val defaultTypeMappings = mapOf(
            TypeKind.INT to "int",
            TypeKind.FLOAT to "float",
            TypeKind.DOUBLE to "float",
            TypeKind.BOOLEAN to "bool"
        )
    }
}