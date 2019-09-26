package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.points.point.PointContract
import ru.is2si.sisi.presentation.points.point.PointFragment
import ru.is2si.sisi.presentation.points.point.PointPresenter

@Module
abstract class PointModule {

    @PerScreen
    @Binds
    abstract fun view(view: PointFragment): PointContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: PointPresenter): PointContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: PointFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}