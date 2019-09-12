package ru.is2si.sisi.presentation.finish

import ru.is2si.sisi.base.BaseContract

interface FinishContract {
    interface View : BaseContract.View {
        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
        fun showFixFinishTime(fixFinishTime: String)
        fun showFinishData(
                maxNormalTime: String,
                amountPenaltyPoints: String,
                preliminaryPoints: String,
                dateTimeFinish:String
        )
    }

    interface Presenter : BaseContract.Presenter {
        fun finishTeam()
    }
}