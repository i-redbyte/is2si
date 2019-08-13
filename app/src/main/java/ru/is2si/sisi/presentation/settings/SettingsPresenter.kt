package ru.is2si.sisi.presentation.settings

import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val rxSchedulers: RxSchedulers
) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {
    override fun start() {

    }
}