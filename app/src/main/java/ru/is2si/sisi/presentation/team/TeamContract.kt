package ru.is2si.sisi.presentation.team

import ru.is2si.sisi.base.BaseContract

interface TeamContract {
    interface View : BaseContract.View {

        fun showLoading()
        fun showError(message: String?)
    }

    interface Presenter : BaseContract.Presenter {
    }
}