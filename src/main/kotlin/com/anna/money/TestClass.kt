package com.anna.money

@PythonDataClass
data class NestedClass (
    val x: Int
)

@PythonDataClass
data class TestClass (
    val test: List<NestedClass> = emptyList(),
    val s: String
)