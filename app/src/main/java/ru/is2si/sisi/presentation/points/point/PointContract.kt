package ru.is2si.sisi.presentation.points.point

import ru.is2si.sisi.base.BaseContract
import ru.is2si.sisi.presentation.model.LocationView

interface PointContract {

    interface View : BaseContract.View {
        fun openCamera()
        fun checkPermission()
        fun showTestCoordinates(location: LocationView)
        fun showPhotoData(location: LocationView)

        fun showError(message: String?, throwable: Throwable)
        fun showLoading()
        fun showMain()
        fun showTestAccuracyCoordinates(location: LocationView,counter:Int)
    }

    interface Presenter : BaseContract.Presenter {
        var location: LocationView?
        var isAccuracy: Boolean
        fun onCameraClick(isTest: Boolean)
        fun getLocation()
        fun addToPhotosQueue(photoPath: String)
        fun permissionOk() // TODO: Red_byte 2019-10-04 rename method
    }
}