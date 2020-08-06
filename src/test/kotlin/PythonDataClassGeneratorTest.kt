package com.anna.money

import com.anna.money.testpackage.AnotherPythonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class PythonDataClassGeneratorTest {
//
//    @Test
//    fun `test python class import is generated correctly`() {
//        assertEquals(
//            "from com.anna.money.python_class import PythonClass",
//            PythonClass::class.toImportString()
//        )
//    }
//
//    @Test
//    fun `snake case name for the class is generated correctly`() {
//        assertEquals(
//            "python_class",
//            getSnakeCaseName(PythonClass::class.simpleName!!)
//        )
//    }
//
//    @Test
//    fun `test class path is generated correctly`() {
//        val outputFolder = "/tmp"
//        assertEquals(
//            "/tmp/com/anna/money/python_class.py",
//            PythonDataClassGenerator(outputFolder).getFilePath(PythonClass::class)
//        )
//    }
//
//    @Test
//    fun `test module file paths are generated correctly`() {
//        val outputFolder = "/tmp"
//        assertEquals(
//            listOf(
//                "/tmp/com/anna/money/__init__.py",
//                "/tmp/com/anna/__init__.py",
//                "/tmp/com/__init__.py"
//            ),
//            PythonDataClassGenerator(outputFolder).getModuleFilesToGenerate(PythonClass::class)
//        )
//    }
//
//    @Test
//    fun `test file is generated in the output dir and modules are initialized`() {
//        val outputFolder = "/tmp/PythonDataClassGeneratorTest"
//        File(outputFolder).deleteRecursively()
//        val generator = PythonDataClassGenerator(outputFolder)
//        generator.generateFiles(listOf(PythonClass::class))
//        mutableListOf(
//            "$outputFolder/com/anna/money/python_class.py"
//        ).apply {
//            addAll(generator.getModuleFilesToGenerate(PythonClass::class))
//        }.map {
//            assertEquals(true, File(it).exists(), "File $it is missing")
//        }
//    }
//
//    @Test
//    fun `test scalar types generation`() {
//        assertEquals(
//            expectedScalarTypesPythonClass,
//            PythonDataClassGenerator("").generateSourceCode(PythonClass::class)
//        )
//    }
//
//    companion object {
//        private val expectedScalarTypesPythonClass =
//            """from dataclasses import dataclass
//
//@dataclass
//class PythonClass:
//    int_val: int
//
//    def validate(self):
//        if not isinstance(self.int_val, int):
//            raise RuntimeError(f'PythonClass.int_val value {self.int_val} should be of type int')
//"""
//    }
//
//    @Test
//    fun `test nested classes generation`() {
//        val outputFolder = "/tmp/PythonDataClassGeneratorTest"
//        File(outputFolder).deleteRecursively()
//        PythonDataClassGenerator(outputFolder).generateFiles(
//            input = listOf(PythonClassWithNestedClass::class)
//        )
//    }
}

@Serializable
data class PythonClass(
    @SerialName("int_val")
    val intVal: Int
)

@Serializable
data class PythonClassWithNestedClass(
    val nested: AnotherPythonClass
)

