package ru.is2si.sisi.base.switcher

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.error_screen.view.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.LoadingType.*
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

    fun switchToError(errorMessage: String?, listener: OnErrorListener) {
        val errorView = layoutInflater.inflate(R.layout.error_screen, container, false)
        errorView.tvError.text = errorMessage
            ?: mainView.context.getString(R.string.state_unknown_error)
        errorView.btnRetry.setOnClickListener {
            listener.invoke()
        }
        addViewState(STATE_ERROR, errorView)
        switchToError()
    }

    fun switchToLoading() = switchToLoading(BLUE)

    fun switchToLoading(type: LoadingType) {
        val loadingView = when (type) {
            BLUE -> layoutInflater.inflate(R.layout.loading_screen, container, false)
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

}

private enum class StateView {
    STATE_MAIN,
    STATE_LOADING,
    STATE_OSAGO_LOADING,
    STATE_ERROR,
    STATE_EMPTY,
    STATE_NOT_FOUND,
    STATE_SUCCESS
}

enum class LoadingType {
    BLUE
}

typealias OnErrorListener = () -> Unit