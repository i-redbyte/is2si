package ru.is2si.sisi.presentation.settings

import ru.is2si.sisi.base.BaseContract

interface SettingsContract {

    interface View : BaseContract.View {
        fun showCurrentUrl(url: String)
        fun backScreen()
    }

    interface Presenter : BaseContract.Presenter {
        fun changeServerUrl(newUrl: String)
    }
}