package com.anna.money

data class PythonImport(
    val packageName: String,
    val typeName: String
) {
    fun toImportString() = "from $packageName import $typeName\n"
}