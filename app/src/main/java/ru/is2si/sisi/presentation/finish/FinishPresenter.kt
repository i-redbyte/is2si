package ru.is2si.sisi.presentation.finish

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.domain.finish.FinishTeam
import javax.inject.Inject

class FinishPresenter @Inject constructor(
        private val finishTeam: FinishTeam
) : BasePresenter<FinishContract.View>(), FinishContract.Presenter {
    override fun start() {

    }
}