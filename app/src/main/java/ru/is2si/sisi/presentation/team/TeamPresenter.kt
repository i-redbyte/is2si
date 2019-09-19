package ru.is2si.sisi.presentation.team

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.auth.Logout
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class TeamPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val logout: Logout,
        private val getSaveTeam: GetSaveTeam
) : BasePresenter<TeamContract.View>(), TeamContract.Presenter {

    override fun start() {
        disposables +=
                getSaveTeam.execute(None)
                        .map{it as CompetitionResult}
                        .map(CompetitionResult::asView)
                        .subscribeOn(rxSchedulers.io)
                        .observeOn(rxSchedulers.ui)
                        .subscribe({ view.setTeam(it) }) { view.showError(it.message) }
    }

    override fun onPhoneClick() = view.phoneCall()

    override fun logout() {
        disposables += logout.execute(None)
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ view.goToMain() }) { view.showError(it.message) }
    }

}