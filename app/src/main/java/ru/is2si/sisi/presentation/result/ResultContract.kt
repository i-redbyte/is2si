package ru.is2si.sisi.presentation.result

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.domain.result.CompetitionResult

interface ResultContract {
    interface View : BaseContract.View {
        fun showResult(competitions: List<CompetitionResult>) // TODO: Red_byte 2019-08-02 change to View model

        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
    }

    interface Presenter : BaseContract.Presenter {
        fun getResult()
    }
}