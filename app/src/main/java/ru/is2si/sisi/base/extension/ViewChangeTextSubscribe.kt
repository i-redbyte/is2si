package ru.is2si.sisi.base.extension

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

class ViewChangeTextSubscribe(val view: TextView) : ObservableOnSubscribe<String> {

    override fun subscribe(emitter: ObservableEmitter<String>) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw IllegalStateException(
                "Must be called on the main thread. Current thread: ${Thread.currentThread()}"
            )
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (emitter.isDisposed.not()) emitter.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable) = Unit
        }

        view.addTextChangedListener(watcher)
        if (emitter.isDisposed) view.removeTextChangedListener(watcher)
        emitter.onNext(view.text.toString())
    }
}