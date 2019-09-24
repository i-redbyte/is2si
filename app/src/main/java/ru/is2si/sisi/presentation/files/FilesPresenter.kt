package ru.is2si.sisi.presentation.files

import android.os.SystemClock
import android.util.Log
import io.reactivex.Observable
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.device.location.Location
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_PHOTOS
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_TRACK
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.auth.GetTeamPin
import ru.is2si.sisi.domain.files.GetCurrentLocation
import ru.is2si.sisi.domain.files.GetFileQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue.Params
import ru.is2si.sisi.domain.files.UploadFile
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.presentation.model.LocationView
import ru.is2si.sisi.presentation.model.asView
import javax.inject.Inject

class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val saveFilePathToQueue: SaveFilePathToQueue,
        private val getFileQueue: GetFileQueue,
        private val getTeamPin: GetTeamPin,
        private val getSaveTeam: GetSaveTeam,
        private val uploadFile: UploadFile,
        private val getCurrentLocation: GetCurrentLocation
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    private var pin = ""
    private var teamName = ""
    @Volatile
    private var isGetLocationInProgress = false
    @Volatile
    private var lastLocationUpdate: Long = SystemClock.elapsedRealtime()
    override var location: LocationView? = null

    override fun start() {
        // TODO: Red_byte 2019-09-11 refactoring this - fast decision
        disposables += getTeamPin.execute(None)
                .flatMap { pin ->
                    getSaveTeam.execute(None)
                            .map { competition ->
                                competition as CompetitionResult
                                pin to competition
                            }
                }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    val (p, team) = it
                    pin = p
                    teamName = team.team?.teamName ?: ""
                }) { view.showError(it.message, it) }
    }

    override fun uploadFiles() {
        view.showLoading()
        disposables += getFileQueue.execute(None)
                .flatMapCompletable {
                    val files = it
                    Observable.fromIterable(files)
                            .flatMapCompletable { filePath ->
                                uploadFile.execute(UploadFile.Params(filePath, pin, teamName, TYPE_PHOTOS))
                            }
                }
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.showMain()
                    view.showSuccessUpload()
                }) { view.showError(it.message, it) }
    }

    override fun uploadTracks(filePath: String) {
        view.showLoading()
        disposables += uploadFile.execute(UploadFile.Params(filePath, pin, teamName, TYPE_TRACK))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.showMain()
                    view.showSuccessUpload()
                }) { view.showError(it.message, it) }
    }

    private fun getLocation() {
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
                .subscribe({
                    // TODO: Red_byte 2019-09-19 test show data
                    Log.d("_debug", "lat: ${it.latitude}")
                    Log.d("_debug", "lon: ${it.longitude}")
                }) { /* no-op */ }
    }

    override fun onCameraClick() {
        getLocation()
        view.openCamera()
    }

    override fun addToPhotosQueue(photoPath: String) {
        disposables += saveFilePathToQueue.execute(Params(photoPath))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ /* no-op */ }) { view.showError(it.message, it) }
    }

}