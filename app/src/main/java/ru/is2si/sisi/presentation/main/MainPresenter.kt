package ru.is2si.sisi.presentation.main

import ru.is2si.sisi.base.BasePresenter
import javax.inject.Inject

class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun start() = Unit
}