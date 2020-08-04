package com.anna.money

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun KClass<*>?.isSerializable() =
    this?.annotations?.any { it is Serializable } ?: false

fun KClass<*>.getNestedSerializableClasses() =
    members.filterIsInstance<KProperty<*>>()
        .map { it.returnType.classifier as KClass<*> }
        .filter { it.isSerializable() }

fun KClass<*>.toImportString() =
    qualifiedName!!.split(".").toMutableList().run {
        set(size - 1, getSnakeCaseName(get(size - 1)))
        joinToString(".")
    }.let { packageImport ->
        "from $packageImport import $simpleName"
    }

