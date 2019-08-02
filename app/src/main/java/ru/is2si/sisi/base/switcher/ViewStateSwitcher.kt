package ru.is2si.sisi.base.switcher

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.error_screen.view.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.switcher.LoadingType.MAIN_YELLOW
import ru.is2si.sisi.base.switcher.StateView.*

class ViewStateSwitcher(activity: Activity, @IdRes idRes: Int) {

    private var mainView: View
    private val states = HashMap<StateView, ViewInfo>()
    private val layoutInflater = activity.layoutInflater
    private val container: ViewGroup

    init {
        mainView = activity.findViewById(idRes)
        container = mainView.parent as ViewGroup
        states[STATE_MAIN] = ViewInfo(0, mainView)
    }

    fun switchToError(errorMessage: String?, throwable: Throwable? = null, listener: OnErrorListener) {
        val errorView = layoutInflater.inflate(R.layout.error_screen, container, false)
        errorView.tvError.text = errorMessage
                ?: mainView.context.getString(R.string.state_unknown_error)
        errorView.btnRetry.setOnClickListener {
            listener.invoke()
        }
        addViewState(STATE_ERROR, errorView)
        switchToError()
        if (throwable != null) buildStackTrace(errorView, throwable)
    }

    fun switchToLoading() = switchToLoading(MAIN_YELLOW)

    fun switchToLoading(type: LoadingType) {
        val loadingView = when (type) {
            MAIN_YELLOW -> layoutInflater.inflate(R.layout.loading_screen, container, false)
        }
        addViewState(STATE_LOADING, loadingView)
        switchToState(STATE_LOADING)
        show(loadingView)
    }

    fun switchToMain() = switchToState(STATE_MAIN)

    private fun switchToError() = switchToState(STATE_ERROR)

    private fun addViewState(state: StateView, stateView: View) {
        stateView.visibility = View.GONE
        states[state] = ViewInfo(0, stateView)
    }

    private fun switchToState(state: StateView) {
        val viewInfo = states[state] ?: return
        if (viewInfo.view == null) {
            viewInfo.view = layoutInflater.inflate(viewInfo.layoutId, container, false)
        }
        val nextView = viewInfo.view
        if (nextView == null || nextView == mainView) return
        if (nextView.parent != container) container.addView(nextView)
        show(nextView)
    }

    private fun show(nextView: View) {
        mainView.visibility = View.GONE
        nextView.visibility = View.VISIBLE
        mainView = nextView
    }

    private class ViewInfo(var layoutId: Int, var view: View?)

    private fun buildStackTrace(error: View, throwable: Throwable) {
        error.findViewById<View>(R.id.tvError)?.onClick {
            error.findViewById<TextView>(R.id.tvError)?.apply {
                text = if (throwable.cause == null) {
                    throwable.message
                } else {
                    throwable.message + "\n" + throwable.cause?.message
                }
                onClick {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val text = StringBuilder()
                            .appendln("Error message:")
                            .appendln(throwable.message)
                            .appendln("Error stack:")
                            .appendln(Log.getStackTraceString(throwable))
                            .apply {
                                if (throwable.cause != null) {
                                    appendln("\nCause Error:")
                                    appendln("Error message:")
                                    appendln(throwable.cause?.message)
                                    appendln("Error stack:")
                                    appendln(Log.getStackTraceString(throwable.cause))
                                }
                            }
                    val clip = ClipData.newPlainText("Error Stack", text)
                    clipboard?.primaryClip = clip
                    Toast.makeText(context, "Error info copied!", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }
}

private enum class StateView {
    STATE_MAIN,
    STATE_LOADING,
    STATE_ERROR,
    STATE_EMPTY,
    STATE_NOT_FOUND
}

enum class LoadingType {
    MAIN_YELLOW
}

typealias OnErrorListener = () -> Unit