package ru.is2si.sisi.presentation.files

import android.util.Log
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.files.GetFileQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue
import ru.is2si.sisi.domain.files.SaveFilePathToQueue.Params
import java.io.File
import java.io.IOException
import javax.inject.Inject


class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers,
        private val saveFilePathToQueue: SaveFilePathToQueue,
        private val getFileQueue: GetFileQueue
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    override fun start() {

    }

    override fun uploadFiles() {
        disposables += getFileQueue.execute(None())
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    Log.d("_debug", "LIST ==== ${it.size}")
                    /* no-op */
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