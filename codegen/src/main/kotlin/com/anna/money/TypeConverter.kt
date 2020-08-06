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
}