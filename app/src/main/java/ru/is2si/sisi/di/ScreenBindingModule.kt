package ru.is2si.sisi.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.auth.AuthFragment
import ru.is2si.sisi.presentation.team.TeamFragment

@Module
interface ScreenBindingModule {

    @PerScreen
    @ContributesAndroidInjector(modules = [AuthModule::class])
    fun bindAuth(): AuthFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [TeamModule::class])
    fun bindTeam(): TeamFragment

}