package ru.is2si.sisi.presentation.points.point

import android.os.SystemClock
import android.util.Log
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.device.location.Location
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.files.GetCurrentLocation
import ru.is2si.sisi.domain.files.SaveFilePathToQueue
import ru.is2si.sisi.domain.files.SubscribeUpdateLocation
import ru.is2si.sisi.presentation.model.LocationView
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class PointPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val saveFilePathToQueue: SaveFilePathToQueue,
        private val getCurrentLocation: GetCurrentLocation,
        private val subscribeUpdateLocation: SubscribeUpdateLocation
) : BasePresenter<PointContract.View>(), PointContract.Presenter {

    @Volatile
    private var isGetLocationInProgress = false
    @Volatile
    private var lastLocationUpdate: Long = SystemClock.elapsedRealtime()
    private var locations = mutableListOf<LocationView>()

    override var location: LocationView? = null
    override var isAccuracy: Boolean = false


    override fun start() {
        view.checkPermission()
    }

    override fun onCameraClick(isTest: Boolean) {
        if (isTest)
            locationData()
        else
            view.openCamera()
    }

    override fun permissionOk() {
        getLocation()
    }

    override fun addToPhotosQueue(photoPath: String) {
        disposables += saveFilePathToQueue.execute(SaveFilePathToQueue.Params(photoPath))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe(::locationData) { view.showError(it.message, it) }
    }

    private fun locationData() {
        if (isAccuracy) accuracy()
        else notAccuracy()
    }

    private fun accuracy() {
        var counter = 0
        disposables += subscribeUpdateLocation.execute(None)
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .take(ACCURACY_COUNT.toLong())
                .doOnComplete { view.showTestCoordinates(getAccurancyLocation()) }
                .doOnNext {
                    counter++
                    val location = it.asView()
                    locations.add(location)
                    Log.d("_debug", "counter == $counter")
                    view.showTestAccuracyCoordinates(location, counter)
                }
                .subscribe({ }) { view.showError(it.message, it) }
    }

    private fun notAccuracy() {
        view.showLoading()
        disposables += getCurrentLocation.execute(None)
                .map(Location::asView)
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe(view::showPhotoData) { view.showError(it.message, it) }
    }

    private fun getAccurancyLocation(): LocationView {
        val sortedList = locations.sortedWith(compareBy({ it.latitude }, { it.longitude }))
        var latitude = 0.0
        var longitude = 0.0
        for (i in 2..ACCURACY_COUNT - 3) {
            latitude += sortedList[i].latitude
            longitude += sortedList[i].longitude
        }
        latitude /= ACCURACY_COUNT - 4
        longitude /= ACCURACY_COUNT - 4
        return LocationView(latitude, longitude)
    }

    override fun getLocation() {
        if (isGetLocationInProgress)
            return
        isGetLocationInProgress = true
        disposables += getCurrentLocation.execute(None)
                .map(Location::asView)
                .doOnSuccess { location = it }
                .doOnSuccess { lastLocationUpdate = SystemClock.elapsedRealtime() }
                .doAfterTerminate { isGetLocationInProgress = false }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe(view::showTestCoordinates) { /* no-op */ }
    }

    companion object {
        private const val ACCURACY_COUNT = 12
    }
}