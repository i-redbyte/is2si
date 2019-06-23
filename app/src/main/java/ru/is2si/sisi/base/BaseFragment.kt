package ru.is2si.sisi.base

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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

}
