package ru.is2si.sisi.presentation.points

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.presentation.model.PointView

interface PointsContract {
    interface View : BaseContract.View {
        fun showPoints(points: List<PointView>)
        fun showSummaryBalls(points: List<PointView>)
        fun showPointsByRemove(position: Int)

        fun showLoading()
        fun showError(message: String?)
        fun showSnack(message: String?)
        fun showMain()
    }

    interface Presenter : BaseContract.Presenter {
        fun getPoints()
        fun addPoint(pointName: String)
        fun removePoint(point: PointView, position: Int)
    }
}