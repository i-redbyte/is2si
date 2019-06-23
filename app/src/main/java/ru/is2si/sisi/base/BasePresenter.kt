package ru.is2si.sisi.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter {
    protected var disposables = CompositeDisposable()

    @Inject
    lateinit var view: V


    operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun stop() {
        disposables.clear()
    }
}