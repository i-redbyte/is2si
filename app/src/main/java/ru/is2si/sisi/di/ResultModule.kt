package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.result.ResultContract
import ru.is2si.sisi.presentation.result.ResultFragment
import ru.is2si.sisi.presentation.result.ResultPresenter

@Module
abstract class ResultModule {

    @PerScreen
    @Binds
    abstract fun view(view: ResultFragment): ResultContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: ResultPresenter): ResultContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: ResultFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}