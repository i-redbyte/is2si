package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.files.FilesContract
import ru.is2si.sisi.presentation.files.FilesFragment
import ru.is2si.sisi.presentation.files.FilesPresenter

@Module
abstract class FilesModule {

    @PerScreen
    @Binds
    abstract fun view(view: FilesFragment): FilesContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: FilesPresenter): FilesContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: FilesFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}