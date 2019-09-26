package ru.is2si.sisi.presentation.points.point

import ru.is2si.sisi.base.BaseContract

interface PointContract {

    interface View : BaseContract.View {

        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
    }

    interface Presenter : BaseContract.Presenter {

    }
}