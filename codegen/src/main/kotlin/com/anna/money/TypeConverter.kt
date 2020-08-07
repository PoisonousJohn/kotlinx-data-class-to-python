package com.anna.money

import javax.lang.model.element.Element


interface TypeConverter {
    fun isTypeSupported(type: Element): Boolean

    /**
     * Returns python type declaration, for example [String] is mapped to `str` in Python
     */
    fun getPythonTypeDeclaration(type: Element): String

    /**
     * Any custom imports required to support this type
     */
    fun getCustomImports(type: Element): List<PythonImport>

    /**
     * Any custom validation which will be placed inside custom `validate` method in python's
     * data class
     *
     * Sample output may be:
     *
     * ```python
     * if not isinstance(self.name, int):
     *     raise RuntimeError(f'Property name should be of type int')
     * ```
     *
     * Output should start with zero-level indentation
     *
     * @param type the type a validation is generated for
     * @param valueGetter a string which in python will return value to validate, e.g. self.$propertyName
     */
    fun getValidationString(valueGetter: String, type: Element): String
}