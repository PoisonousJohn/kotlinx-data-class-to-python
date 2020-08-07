package com.anna.money

import com.sun.xml.internal.ws.server.sei.ValueGetter
import javax.lang.model.element.Element

class GenericListTypeConverter : TypeConverter {
    override fun isTypeSupported(type: Element): Boolean =
        type.baseGenericType()?.qualifiedName?.toString() == listQualifier

    override fun getPythonTypeDeclaration(type: Element): String {
        return "List[${getGenericTypeParameterPythonType(type)}]"
    }

    override fun getCustomImports(type: Element): List<PythonImport> = listOf(
        PythonImport(packageName = "typing", typeName = "List")
    ) + type.genericTypeParameter().let { genericTypeParameter ->
        getGenericTypeParameterConverter(genericTypeParameter).getCustomImports(genericTypeParameter)
    }

    override fun getValidationString(valueGetter: String, type: Element) =
        PythonLangHelpers.validateTypeInList(valueGetter, getGenericTypeParameterPythonType(type))

    private fun getGenericTypeParameterPythonType(type: Element) =
        type.genericTypeParameter().let { typeParameter ->
            getGenericTypeParameterConverter(typeParameter).getPythonTypeDeclaration(typeParameter)
        }

    private fun getGenericTypeParameterConverter(genericTypeParameter: Element): TypeConverter {
        if (genericTypeParameter.hasAnnotation(PythonDataClass::class.java.name))
            return SerializableTypeConverter()
        return FileContext.defaultTypeConverters
            .firstOrNull { it.isTypeSupported(genericTypeParameter) }
            ?: throw IllegalArgumentException("Didn't find converter for generic type parameter $genericTypeParameter")
    }

    companion object {
        const val listQualifier = "java.util.List"
    }
}