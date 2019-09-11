package ru.is2si.sisi.presentation.files

import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import ru.is2si.sisi.base.rx.RxSchedulers
import java.io.File
import java.io.IOException
import javax.inject.Inject


class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    override fun start() {

    }

    override fun uploadFiles() {

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

    }

}