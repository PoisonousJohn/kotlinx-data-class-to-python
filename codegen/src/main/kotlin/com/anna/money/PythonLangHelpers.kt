package com.anna.money

class PythonLangHelpers {
    companion object {
        fun String.indentBy(level: Int) =
            split("\n")
                .filter { it.isNotBlank() }.joinToString("\n") {
                    "${"    ".repeat(level)}$it"
                }

        fun validateType(valueGetter: String, typeToValidate: String) =
            "if not isinstance($valueGetter, $typeToValidate):\n    raise RuntimeError(f'Property $valueGetter value {$valueGetter} should be of type $typeToValidate')"

        fun validateTypeInList(valueGetter: String, typeToValidate: String) =
            "for element in $valueGetter:\n${validateType(
                "element",
                typeToValidate
            ).indentBy(1)}"
    }
}