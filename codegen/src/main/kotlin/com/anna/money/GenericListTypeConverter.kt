package com.anna.money

import javax.lang.model.element.Element

class GenericListTypeConverter : TypeConverter {
    override fun isTypeSupported(type: Element): Boolean =
        type.baseGenericType()?.qualifiedName?.toString() == listQualifier

    override fun getPythonTypeDeclaration(type: Element): String {
        val genericTypeParameter = type.genericTypeParameter()
        val pythonType = getGenericTypeParameterConverter(genericTypeParameter)
            .getPythonTypeDeclaration(genericTypeParameter)
        return "List[$pythonType]"
    }

    private fun getGenericTypeParameterConverter(genericTypeParameter: Element): TypeConverter {
        if (genericTypeParameter.hasAnnotation(PythonDataClass::class.java.name))
            return SerializableTypeConverter()
        return FileContext.defaultTypeConverters
            .firstOrNull { it.isTypeSupported(genericTypeParameter) }
            ?: throw IllegalArgumentException("Didn't find converter for generic type parameter $genericTypeParameter")
    }

    override fun getCustomImports(type: Element): List<PythonImport> = listOf(
        PythonImport(packageName = "typing", typeName = "List")
    ) + type.genericTypeParameter().let { genericTypeParameter ->
        getGenericTypeParameterConverter(genericTypeParameter).getCustomImports(genericTypeParameter)
    }

    companion object {
        const val listQualifier = "java.util.List"
    }
}