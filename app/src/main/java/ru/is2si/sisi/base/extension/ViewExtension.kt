@file:Suppress("NOTHING_TO_INLINE")

package ru.is2si.sisi.base.extension

import android.content.Context
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.Observable

inline fun View.show() {
    visibility = View.VISIBLE
}

inline fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.hide() {
    visibility = View.GONE
}

inline fun View.isHidden(): Boolean = visibility == View.GONE

fun TextInputEditText?.showKeyboard() {
    this ?: return
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText?.showKeyboard() {
    this ?: return
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun SearchView?.showKeyboard() {
    this ?: return
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

inline fun TextInputEditText.afterTextChanged(
        crossinline afterTextChanged: (string: Editable) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) = afterTextChanged(s)

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
    }
    addTextChangedListener(textWatcher)
    return textWatcher

}

inline fun TextInputEditText.onTextChanged(
        crossinline onTextChanged: (string: CharSequence) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) = Unit

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = onTextChanged(s)
    }
    addTextChangedListener(textWatcher)
    return textWatcher

}

inline fun SearchView.onQueryTextChanged (
        crossinline onQueryTextChanged: (string: String) -> Unit
) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            onQueryTextChanged(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }
    })
}

inline fun TextView.textChanges(): Observable<String> = Observable.create(ViewChangeTextSubscribe(this))

inline fun TextInputEditText.textChanges(): Observable<String> = Observable.create(ViewChangeTextSubscribe(this))

inline fun AppCompatAutoCompleteTextView.onTextChangedForAuto(
        crossinline onTextChanged: (string: CharSequence) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) = Unit

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = onTextChanged(s)
    }
    addTextChangedListener(textWatcher)
    return textWatcher

}

inline fun View.onClick(delayMillis: Long = 500, crossinline clickListener: (View) -> Unit) {
    var clickMillis = 0L
    setOnClickListener {
        val elapsedRealTime = SystemClock.elapsedRealtime()
        if (elapsedRealTime > clickMillis) {
            clickMillis = elapsedRealTime + delayMillis
            clickListener.invoke(it)
        }
    }
}

val RecyclerView.ViewHolder.context: Context
    get() = itemView.context