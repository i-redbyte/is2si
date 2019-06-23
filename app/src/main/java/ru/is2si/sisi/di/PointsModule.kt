package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.points.PointsContract
import ru.is2si.sisi.presentation.points.PointsFragment
import ru.is2si.sisi.presentation.points.PointsPresenter

@Module
abstract class PointsModule {

    @PerScreen
    @Binds
    abstract fun view(view: PointsFragment): PointsContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: PointsPresenter): PointsContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: PointsFragment) =
            ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}