package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.navigation.FragmentNavigator
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.auth.AuthContract
import ru.is2si.sisi.presentation.auth.AuthFragment
import ru.is2si.sisi.presentation.auth.AuthPresenter

@Module
abstract class AuthModule {

    @PerScreen
    @Binds
    abstract fun view(view: AuthFragment): AuthContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: AuthPresenter): AuthContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun navigator(view: AuthFragment) =
                FragmentNavigator(view.requireFragmentManager(), R.id.vgContainer)

        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: AuthFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}