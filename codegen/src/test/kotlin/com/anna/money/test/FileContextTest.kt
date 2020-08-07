package com.anna.money.test

import com.anna.money.FileContext
import com.anna.money.PythonDataClass
import com.anna.money.PythonLangHelpers
import com.anna.money.PythonLangHelpers.Companion.indentBy
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import kotlin.test.assertEquals

class FileContextTest {
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var element: TypeElement
    @Mock
    private lateinit var typeMirror: TypeMirror
    @Mock
    private lateinit var declaredType: DeclaredType
    @Mock
    private lateinit var annotationMirror: AnnotationMirror

    @Test
    fun `test indent`() {
        val input = "line1\nasdf    line2"
        val expected = "    line1\n    asdf    line2"
        val expected2 = "        line1\n        asdf    line2"
        assertEquals(expected, input.indentBy(1))
        assertEquals(expected2, input.indentBy(2))
    }

    @Test
    fun `test getPackage`() {
        whenever(element.qualifiedName).thenReturn(StubName("com.anna.money.TestType"))
        whenever(element.simpleName).thenReturn(StubName("TestType"))
        whenever(typeMirror.toString()).thenReturn("com.anna.money.TestType")
        whenever(declaredType.toString()).thenReturn(PythonDataClass::class.java.name)
        whenever(annotationMirror.annotationType).thenReturn(declaredType)
        whenever(element.annotationMirrors).thenReturn(listOf(annotationMirror))
        whenever(element.asType()).thenReturn(typeMirror)
        val expectedDirs = setOf(
            "com",
            "com/anna",
            "com/anna/money"
        )
        assertEquals(expectedDirs, FileContext(element).getModuleDirs())
    }

}
