package ru.is2si.sisi.presentation.team

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.domain.auth.Logout
import ru.is2si.sisi.presentation.model.TeamView

interface TeamContract {
    interface View : BaseContract.View {
        fun phoneCall()
        fun setTeam(team: TeamView)

        fun showLoading()
        fun showError(message: String?)
        fun goToMain()
    }

    interface Presenter : BaseContract.Presenter {
        fun onPhoneClick()
        fun logout()
}
}