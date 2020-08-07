package com.anna.money

import javax.lang.model.element.Element

class SerializableTypeConverter : TypeConverter {
    override fun isTypeSupported(type: Element): Boolean =
        type.hasAnnotation(PythonDataClass::class.java.name)

    override fun getPythonTypeDeclaration(type: Element): String {
        return type.simpleName.toString()
    }

    override fun getCustomImports(type: Element): List<PythonImport> = listOf(type.toPythonImport())

    override fun getValidationString(valueGetter: String, type: Element) =
        PythonLangHelpers.validateType(valueGetter, type.simpleName.toString())
}