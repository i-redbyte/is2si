package ru.is2si.sisi.presentation.points

import io.reactivex.Single
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.presentation.model.PointView
import javax.inject.Inject

class PointsPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers
) : BasePresenter<PointsContract.View>(), PointsContract.Presenter {

    override val points: MutableList<PointView> = mutableListOf()

    override fun start() {
        getPoints()
    }

    override fun getPoints() {
        // TODO: RB 2019-06-23 release it
    }

    override fun addPoint(pointName: Int) {
        disposables += Single.just(PointView(pointName, (1..50).random()))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    if (pointName % 2 == 0) {
                        points.add(it)
                        view.showPoints(points)
                    }

                }) { view.showError(it.message) }
    }

    override fun removePoint(position: Int) {
        points.removeAt(position)
        view.showPointsByRemove(position)
    }

}