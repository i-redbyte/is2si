package ru.is2si.sisi.presentation.points.point

import android.os.SystemClock
import android.util.Log
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.device.location.Location
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase
import ru.is2si.sisi.domain.files.GetCurrentLocation
import ru.is2si.sisi.domain.files.SaveFilePathToQueue
import ru.is2si.sisi.presentation.model.LocationView
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class PointPresenter @Inject constructor(
    private val rxSchedulers: RxSchedulers,
    private val saveFilePathToQueue: SaveFilePathToQueue,
    private val getCurrentLocation: GetCurrentLocation

) : BasePresenter<PointContract.View>(), PointContract.Presenter {

    @Volatile
    private var isGetLocationInProgress = false
    @Volatile
    private var lastLocationUpdate: Long = SystemClock.elapsedRealtime()
    override var location: LocationView? = null

    override fun start() {

    }

    override fun onCameraClick() {
        getLocation()
        view.openCamera()
    }

    override fun addToPhotosQueue(photoPath: String) {
        disposables += saveFilePathToQueue.execute(SaveFilePathToQueue.Params(photoPath))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ /* no-op */ }) { view.showError(it.message, it) }
    }


    private fun getLocation() {
        if (isGetLocationInProgress)
            return
        isGetLocationInProgress = true
        disposables += getCurrentLocation.execute(UseCase.None)
                .map(Location::asView)
                .doOnSuccess { location = it }
                .doOnSuccess { lastLocationUpdate = SystemClock.elapsedRealtime() }
                .doAfterTerminate { isGetLocationInProgress = false }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    // TODO: Red_byte 2019-09-19 test show data
                    Log.d("_debug", "lat: ${it.latitude}")
                    Log.d("_debug", "lon: ${it.longitude}")
                }) { /* no-op */ }
    }
}