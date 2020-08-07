package com.anna.money.test

import com.anna.money.getPackage
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import javax.lang.model.element.TypeElement
import kotlin.test.assertEquals

class ElementExtensionsTest {
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var element: TypeElement

    @Test
    fun `test getPackage`() {
        whenever(element.qualifiedName).thenReturn(StubName("com.anna.money.TestType"))
        assertEquals("com.anna.money", element.getPackage())
    }
}