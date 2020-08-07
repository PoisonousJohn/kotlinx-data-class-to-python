package com.anna.money

import kotlinx.serialization.SerialName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

fun Element.baseGenericType() = (this.asType() as? DeclaredType)?.asElement() as? TypeElement
fun Element.getFields() =
    enclosedElements.filter { it.kind == ElementKind.FIELD }

fun Element.genericTypeParameters(): MutableList<out TypeMirror> = (this.asType() as DeclaredType).typeArguments
fun Element.genericTypeParameter(): Element = (this.genericTypeParameters()[0] as DeclaredType).asElement()
fun Element.hasAnnotation(annotation: String) = this.annotationMirrors.any {
    it.annotationType.toString() == annotation
}


fun Element.getSerializedName() = getAnnotation(SerialName::class.java)?.value
    ?: getSnakeCaseName(simpleName.toString())

fun TypeElement.getPackage() = qualifiedName.split(".").toMutableList().apply {
    removeAt(size - 1)
}.joinToString(".")

//fun TypeElement.toImportString() =
//    qualifiedName!!.split(".").toMutableList().run {
//        set(size - 1, getSnakeCaseName(get(size - 1)))
//        joinToString(".")
//    }.let { packageImport ->
//        "from $packageImport import $simpleName"
//    }

fun Element.toPythonImport() =
        PythonImport(
            packageName = "${(this as TypeElement).getPackage()}.${getSnakeCaseName(simpleName.toString())}",
            typeName = simpleName.toString()
        )

