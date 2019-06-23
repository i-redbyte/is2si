package ru.is2si.sisi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.R
import ru.is2si.sisi.base.navigation.FragmentNavigator
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.team.TeamContract
import ru.is2si.sisi.presentation.team.TeamFragment
import ru.is2si.sisi.presentation.team.TeamPresenter

@Module
abstract class TeamModule {

    @PerScreen
    @Binds
    abstract fun view(view: TeamFragment): TeamContract.View

    @PerScreen
    @Binds
    abstract fun presenter(presenter: TeamPresenter): TeamContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerScreen
        fun navigator(view: TeamFragment) =
                FragmentNavigator(view.requireFragmentManager(), R.id.vgContainer)

        @JvmStatic
        @Provides
        @PerScreen
        fun stateSwitcher(view: TeamFragment) =
                ViewStateSwitcher(view.requireActivity(), R.id.vgContainer)
    }
}