package ru.is2si.sisi.presentation.result

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.presentation.model.CompetitionResultView

interface ResultContract {
    interface View : BaseContract.View {
        fun showResults(competitions: List<CompetitionResultView>) // TODO: Red_byte 2019-08-02 change to View model
        fun stopRefresh()

        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
        fun showCompetitionData(data: CompetitionResultView)
    }

    interface Presenter : BaseContract.Presenter {
        fun getTeamData()
        fun getResult()
        fun getResults()
    }
}