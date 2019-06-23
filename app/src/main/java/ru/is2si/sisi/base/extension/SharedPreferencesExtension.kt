package ru.is2si.sisi.base.extension

import android.annotation.SuppressLint
import android.content.SharedPreferences

@SuppressLint("ApplySharedPref")
@Suppress("NOTHING_TO_INLINE")
inline fun SharedPreferences.commit(crossinline block: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    block.invoke(editor)
    editor.commit()
}

@SuppressLint("ApplySharedPref")
@Suppress("NOTHING_TO_INLINE")
inline fun SharedPreferences.saveAsync(crossinline block: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    block.invoke(editor)
    editor.apply()
}