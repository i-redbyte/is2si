package ru.is2si.sisi.presentation.result

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.domain.result.GetResult
import ru.is2si.sisi.domain.result.GetResults
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class ResultPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val getResult: GetResult,
        private val getResults: GetResults,
        private val getSaveTeam: GetSaveTeam
) : BasePresenter<ResultContract.View>(), ResultContract.Presenter {
    private var competitionId: Int = -1

    override fun start() {
        getTeamData()
    }

    override fun getTeamData() {
        view.showLoading()
        disposables += getSaveTeam.execute(None)
                .map { if (it is CompetitionResult) it else throw RuntimeException("Ошибка! Неудается получить текущее соревнование.") } // TODO: Red_byte 2019-08-31 Вынести в кастомную ошибку
                .map(CompetitionResult::asView)
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .doAfterSuccess { getResults() }
                .subscribe({
                    view.showMain()
                    competitionId = it.competition?.id
                            ?: throw RuntimeException("Ошибка! Неудается получить текущее соревнование.") // TODO: Red_byte 2019-08-31 Вынести в кастомную ошибку
                    view.showCompetitionData(it)
                }) {view.showError(it.message, it) }
    }

    override fun getResults() {
        disposables += getResults.execute(GetResults.Params(competitionId))
                .map { it.map { result -> result.asView() } }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.stopRefresh()
                    view.showResults(it)
                }) {
                    view.stopRefresh()
                    view.showError(it.message, it)
                }
    }

    override fun getResult() {
        disposables += getResult.execute(None)
                .map { it.map { result -> result.asView() } }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ view.showResults(it) }) { view.showError(it.message, it) }
    }

}