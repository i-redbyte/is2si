package ru.is2si.sisi.base

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<P : BaseContract.Presenter> : DaggerFragment() {

    @Inject
    lateinit var presenter: P
    private var isCommitsAllowed: Boolean = false
    private val resumeFragmentActions: MutableList<Runnable> by lazy { mutableListOf<Runnable>() }

    private var resultAction: Runnable? = null

    inline fun executeResumeFragmentAction(crossinline action: () -> Unit) =
            executeResumeFragmentAction(Runnable { action.invoke() })

    fun executeResumeFragmentAction(action: Runnable) {
        if (isCommitsAllowed) action.run() else resumeFragmentActions.add(action)
    }

    inline fun executeResultAction(crossinline action: () -> Unit) =
            executeResultAction(Runnable { action.invoke() })

    fun executeResultAction(action: Runnable) =
            if (isHidden.not()) action.run() else resultAction = action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSoftInputMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    private fun setSoftInputMode() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val density = metrics.densityDpi
        if (density <= DisplayMetrics.DENSITY_HIGH) {
            requireActivity()
                    .window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            requireActivity().window.attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        }
    }

}
