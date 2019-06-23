package ru.is2si.sisi.presentation.auth

import io.reactivex.Single
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import java.lang.RuntimeException
import javax.inject.Inject

class AuthPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers
) : BasePresenter<AuthContract.View>(), AuthContract.Presenter {
    override fun start() {

    }

    override fun authForPinCode(pinCode: String) {
        disposables += Single.just(pinCode)
                .map {
                    if (it == "123")
                        it
                    else
                        throw RuntimeException("Неверный пароль")
                }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({view.gotoTeamScreen()}){view.showError(it.message)}
    }
}