package ru.is2si.sisi.presentation.files

import android.util.Log
import io.reactivex.Observable
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_PHOTOS
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_TRACK
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.auth.GetTeamPin
import ru.is2si.sisi.domain.files.GetFileQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue.Params
import ru.is2si.sisi.domain.files.UploadFile
import ru.is2si.sisi.domain.result.CompetitionResult
import java.io.File
import java.io.IOException
import javax.inject.Inject


class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val saveFilePathToQueue: SaveFilePathToQueue,
        private val getFileQueue: GetFileQueue,
        private val getTeamPin: GetTeamPin,
        private val getSaveTeam: GetSaveTeam,
        private val uploadFile: UploadFile
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    private var pin = ""
    private var teamName = ""

    override fun start() {
        // TODO: Red_byte 2019-09-11 refactoring this - fast decision
        disposables += getTeamPin.execute(None())
                .flatMap { pin ->
                    getSaveTeam.execute(None())
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
        disposables += getFileQueue.execute(None())
                .flatMapCompletable {
                    val files = it
                    if (files.isEmpty()) throw RuntimeException("Нет файлов для отправки на сервер")
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
        Log.d("_debug","filePath ===$filePath")
        disposables += uploadFile.execute(UploadFile.Params(filePath, pin, teamName, TYPE_TRACK))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    view.showMain()
                    view.showSuccessUpload()
                }) { view.showError(it.message, it) }

    }

    override fun onCameraClick() {
        view.openCamera()
    }

    @Throws(IOException::class)
    override fun createPhoto(filesDir: File): File = File.createTempFile(
            LocalDateTime.now().getDateTimeOfPattern("dd_LL_YYYY_HH_mm_ss"),
            ".jpg",
            filesDir
    )

    override fun addToPhotosQueue(photoPath: String) {
        disposables += saveFilePathToQueue.execute(Params(photoPath))
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({ /* no-op */ }) { view.showError(it.message, it) }
    }

}