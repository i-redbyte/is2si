package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.main.MainContract
import ru.is2si.sisi.presentation.main.MainFragment
import ru.is2si.sisi.presentation.main.MainPresenter

@Module
abstract class MainModule {

    @PerScreen
    @Binds
    abstract fun view(view: MainFragment): MainContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: MainPresenter): MainContract.Presenter

}