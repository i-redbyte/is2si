package ru.is2si.sisi.presentation.result

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.result.GetResult
import javax.inject.Inject

class ResultPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val getResult: GetResult
) : BasePresenter<ResultContract.View>(), ResultContract.Presenter {

    override fun start() = Unit

    override fun getResult() {
        disposables += getResult.execute(None())
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ view.showResult(it) }) { view.showError(it.message, it) }
    }

}