package ru.is2si.sisi.presentation.files

import ru.is2si.sisi.base.BaseContract

interface FilesContract {
    interface View : BaseContract.View {
        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
    }

    interface Presenter : BaseContract.Presenter {

    }
}