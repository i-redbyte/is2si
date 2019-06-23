package ru.is2si.sisi.presentation.team

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.presentation.model.TeamView
import javax.inject.Inject

class TeamPresenter @Inject constructor(
    rxSchedulers: RxSchedulers
) : BasePresenter<TeamContract.View>(), TeamContract.Presenter {

    override fun start() {
        view.setTeam(TeamView("Донские Кони"))
    }

    override fun onPhoneClick() = view.phoneCall()


}