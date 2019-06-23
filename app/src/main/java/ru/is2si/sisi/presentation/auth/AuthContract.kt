package ru.is2si.sisi.presentation.auth

import ru.is2si.sisi.base.BaseContract

interface AuthContract {
    interface View : BaseContract.View {
        fun gotoTeamScreen()
        fun showLoading()
        fun showError(message: String?)
    }

    interface Presenter : BaseContract.Presenter {
        fun authForPinCode(pinCode: String)
    }
}