package ru.is2si.sisi.presentation.files

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.presentation.model.LocationView
import java.io.File

interface FilesContract {
    interface View : BaseContract.View {
        fun showError(message: String?, throwable: Throwable)
        fun showError(message: String)
        fun showLoading()
        fun showMain()
        fun openCamera()
        fun showSuccessUpload()
    }

    interface Presenter : BaseContract.Presenter {
        var location: LocationView?
        fun uploadFiles()
        fun onCameraClick()
        fun addToPhotosQueue(photoPath: String)
        fun uploadTracks(filePath: String)
    }
}