package ru.is2si.sisi.presentation.points.point

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import javax.inject.Inject

class PointPresenter @Inject constructor(
    private val rxSchedulers: RxSchedulers
) : BasePresenter<PointContract.View>(), PointContract.Presenter {

    override fun start() {

    }

}