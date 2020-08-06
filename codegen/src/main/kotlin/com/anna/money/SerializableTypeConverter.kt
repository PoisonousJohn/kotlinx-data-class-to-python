package com.anna.money

import javax.lang.model.element.Element

class SerializableTypeConverter : TypeConverter {
    override fun isTypeSupported(type: Element): Boolean =
        type.hasAnnotation("kotlinx.serialization.Serializable")

    override fun getPythonTypeDeclaration(type: Element): String {
        return type.simpleName.toString()
    }

    override fun getCustomImports(type: Element): List<PythonImport> = listOf(type.toPythonImport())
}