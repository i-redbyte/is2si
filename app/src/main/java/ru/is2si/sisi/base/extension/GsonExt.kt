package ru.is2si.sisi.base.extension

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> typeTokenOf(): Type = object : TypeToken<T>() {}.type