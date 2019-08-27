package ru.is2si.sisi.presentation.points

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.points.GetAllPoints
import ru.is2si.sisi.domain.points.GetSelectPoints
import ru.is2si.sisi.domain.points.RemoveSelectPoint
import ru.is2si.sisi.domain.points.SaveSelectPoint
import ru.is2si.sisi.presentation.model.PointView
import ru.is2si.sisi.presentation.model.asDomain
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class PointsPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val getSelectPoints: GetSelectPoints,
        private val getAllPoints: GetAllPoints,
        private val saveSelectPoint: SaveSelectPoint,
        private val removeSelectPoint: RemoveSelectPoint
) : BasePresenter<PointsContract.View>(), PointsContract.Presenter {

    override val points: MutableList<PointView> = mutableListOf()

    override fun start() {
        getPoints()
    }

    override fun getPoints() {
        disposables += getSelectPoints.execute(None())
                .map { it.map { selectPoint -> selectPoint.asView() } }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.showPoints(it)
                }) { view.showError(it.message) }
    }

    override fun addPoint(pointName: String) {
        disposables += saveSelectPoint.execute(SaveSelectPoint.Params(pointName.toInt()))
                .map { it.map { point -> point.asView() } }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    points.clear()
                    points.addAll(it)
                }) { view.showToast(it.message) }

        /*        disposables += Single.just(PointView(pointName, (1..50).random()))
        .subscribeOn(rxSchedulers.io)
        .observeOn(rxSchedulers.ui)
        .subscribe({
            if (pointName % 2 == 0) {
                points.add(it)
                view.showPoints(points)
            }

        }) { view.showError(it.message) }*/
    }

    override fun removePoint(point: PointView, position: Int) {
        disposables += removeSelectPoint.execute(RemoveSelectPoint.Param(point.asDomain()))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    points.remove(point)
                    view.showPointsByRemove(position)
                }) { view.showError(it.message) }
/*        points.removeAt(position)
        view.showPointsByRemove(position)*/
    }


/*    @Suppress("NOTHING_TO_INLINE")
    private inline fun Single<List<ProductView>>.getFavoriteIds(): Single<List<ProductView>> {
        return flatMap { products ->
            val productIds = products
                    .map { it.id }
                    .toSet()
            isProductsInFavorites.execute(IsProductsInFavorite.Params(productIds))
                    .subscribeOn(rxSchedulers.io)
                    .observeOn(rxSchedulers.ui)
                    .doOnSuccess { view.showFavorites(it) }
                    .observeOn(rxSchedulers.io)
                    .map { products }
        }
    }
*/
}