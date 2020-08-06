package com.anna.money

import kotlinx.serialization.SerialName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

fun Element.baseGenericType() = (this.asType() as? DeclaredType)?.asElement() as? TypeElement
fun Element.getFields() =
    enclosedElements.filter { it.kind == ElementKind.FIELD }

fun Element.genericTypeParameters() = (this.asType() as DeclaredType).typeArguments
fun Element.genericTypeParameter() = (this.genericTypeParameters()[0] as DeclaredType).asElement()
fun Element.hasAnnotation(annotation: String) = this.annotationMirrors.any {
    it.annotationType.toString() == annotation
}

fun Element.getAnnotation(annotation: String) = this.annotationMirrors.first {
    it.annotationType.toString() == annotation
}

fun Element.getSerializedName() = getAnnotation(SerialName::class.java)?.value
    ?: getSnakeCaseName(simpleName.toString())

//fun TypeElement.toImportString() =
//    qualifiedName!!.split(".").toMutableList().run {
//        set(size - 1, getSnakeCaseName(get(size - 1)))
//        joinToString(".")
//    }.let { packageImport ->
//        "from $packageImport import $simpleName"
//    }

fun Element.toPythonImport() = ((asType() as DeclaredType).asElement() as TypeElement)
    .qualifiedName!!.split(".").toMutableList().run {
        set(size - 1, getSnakeCaseName(get(size - 1)))
        joinToString(".")
    }.let { packageImport ->
        PythonImport(packageName = packageImport, typeName = simpleName.toString())
    }

