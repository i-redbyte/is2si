package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.settings.SettingsContract
import ru.is2si.sisi.presentation.settings.SettingsFragment
import ru.is2si.sisi.presentation.settings.SettingsPresenter

@Module
abstract class SettingsModule {

    @PerScreen
    @Binds
    abstract fun view(view: SettingsFragment): SettingsContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: SettingsPresenter): SettingsContract.Presenter

}