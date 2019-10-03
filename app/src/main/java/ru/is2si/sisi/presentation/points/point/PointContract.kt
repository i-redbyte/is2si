package ru.is2si.sisi.presentation.points.point

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.presentation.model.LocationView

interface PointContract {

    interface View : BaseContract.View {
        fun openCamera()

        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
    }

    interface Presenter : BaseContract.Presenter {
        var location: LocationView?
        fun onCameraClick()
        fun addToPhotosQueue(photoPath: String)
    }
}