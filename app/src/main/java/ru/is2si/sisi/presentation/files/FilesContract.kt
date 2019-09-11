package ru.is2si.sisi.presentation.files

import ru.is2si.sisi.base.BaseContract
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
        fun uploadFiles()
        fun onCameraClick()
        fun addToPhotosQueue(photoPath: String)
        fun uploadTracks(filePath:String)
        fun createPhoto(filesDir: File): File
    }
}