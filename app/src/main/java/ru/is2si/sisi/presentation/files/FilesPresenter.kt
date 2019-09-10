package ru.is2si.sisi.presentation.files

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import javax.inject.Inject

class FilesPresenter @Inject constructor(
        private val rxSchedulers: RxSchedulers
) : BasePresenter<FilesContract.View>(), FilesContract.Presenter {

    override fun start() {

    }

    override fun uploadFiles() {

    }

}