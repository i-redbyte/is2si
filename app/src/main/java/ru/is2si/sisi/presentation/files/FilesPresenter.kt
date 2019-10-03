package ru.is2si.sisi.presentation.files

import io.reactivex.Observable
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_PHOTOS
import ru.is2si.sisi.data.files.FilesRepository.Companion.TYPE_TRACK
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.domain.auth.GetTeamPin
import ru.is2si.sisi.domain.files.GetFileQueue
import ru.is2si.sisi.domain.files.UploadFile
import ru.is2si.sisi.domain.files.UploadFiles
import ru.is2si.sisi.domain.result.CompetitionResult
import javax.inject.Inject

class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val getFileQueue: GetFileQueue,
        private val getTeamPin: GetTeamPin,
        private val getSaveTeam: GetSaveTeam,
        private val uploadFile: UploadFile,
        private val uploadFiles: UploadFiles
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    private var pin = ""
    private var teamName = ""


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

// TODO: Red_byte 2019-09-24 uncoment and change or remove
//    override fun uploadFiles() {
//        view.showLoading()
//        disposables += getFileQueue.execute(None)
//                .flatMapCompletable {
//                    uploadFiles.execute(UploadFiles.Params(it, pin, teamName, TYPE_PHOTOS))
//                }
//                .subscribeOn(rxSchedulers.io)
//                .observeOn(rxSchedulers.ui)
//                .subscribe({
//                    view.showMain()
//                    view.showSuccessUpload()
//                }) { view.showError(it.message, it) }
//    }

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

}