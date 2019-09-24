package ru.is2si.sisi.presentation.auth

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.AuthTeam
import ru.is2si.sisi.domain.auth.AuthTeam.Param
import ru.is2si.sisi.domain.auth.GetPointsNullException
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.result.EmptyCompetitionResult
import javax.inject.Inject

class AuthPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val authTeam: AuthTeam,
        private val getSaveTeam: GetSaveTeam
) : BasePresenter<AuthContract.View>(), AuthContract.Presenter {

    override fun start() {
        disposables += getSaveTeam.execute(None)
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    if (it !is EmptyCompetitionResult) view.gotoTeamScreen()
                }) { view.showError(it.message, it) }
    }

    override fun authForPinCode(pinCode: String) {
        view.showLoading()
        disposables += authTeam.execute(Param(pinCode))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.showMain()
                    view.gotoTeamScreen()
                }) {
                    if (it is GetPointsNullException) {
                        view.showEmptyPointsToast(it.message)
                        view.showMain()
                        view.gotoTeamScreen()
                    } else
                        view.showError(it.message, it)
                }
    }
}