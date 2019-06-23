package ru.is2si.sisi.presentation.main

import ru.is2si.sisi.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        fun showContent()
        fun showLoading()
        fun showError(throwable: Throwable)
    }

    interface Presenter : BaseContract.Presenter {}
}