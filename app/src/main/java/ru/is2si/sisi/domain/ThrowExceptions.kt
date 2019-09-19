package ru.is2si.sisi.domain

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@Suppress("unused")
internal annotation class ThrowExceptions(vararg val exceptions: KClass<*>)