package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.finish.FinishContract
import ru.is2si.sisi.presentation.finish.FinishFragment
import ru.is2si.sisi.presentation.finish.FinishPresenter

@Module
abstract class FinishModule {

    @PerScreen
    @Binds
    abstract fun view(view: FinishFragment): FinishContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: FinishPresenter): FinishContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: FinishFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}