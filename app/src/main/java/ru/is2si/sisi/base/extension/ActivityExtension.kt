package ru.is2si.sisi.base.extension

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.is2si.sisi.R

fun Activity?.hideKeyboard() {
    this ?: return
    val view = currentFocus ?: return
    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun AppCompatActivity.setActionBar(
        toolbar: Toolbar?,
        crossinline block: ActionBar.() -> Unit
) {
    setSupportActionBar(toolbar)
    supportActionBar?.block()
}

inline fun Fragment.setActionBar(
        toolbar: Toolbar?,
        crossinline block: ActionBar.() -> Unit
) = requireCompatActivity.setActionBar(toolbar, block)

val Fragment.requireCompatActivity: AppCompatActivity
    get() = requireActivity() as AppCompatActivity

inline fun FragmentManager.commitTransactionNow(crossinline block: FragmentTransaction.() -> Unit) {
    val transaction = beginTransaction()
    block.invoke(transaction)
    transaction.commitNow()
}

inline fun <T : Fragment> T.withArguments(crossinline block: Bundle.() -> Unit): T {
    val bundle = Bundle()
    block.invoke(bundle)
    arguments = bundle
    return this
}