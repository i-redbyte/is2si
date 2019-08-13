package ru.is2si.sisi.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.is2si.sisi.di.common.PerScreen
import ru.is2si.sisi.presentation.auth.AuthFragment
import ru.is2si.sisi.presentation.main.MainFragment
import ru.is2si.sisi.presentation.points.PointsFragment
import ru.is2si.sisi.presentation.result.ResultFragment
import ru.is2si.sisi.presentation.settings.SettingsFragment
import ru.is2si.sisi.presentation.team.TeamFragment

@Module
interface ScreenBindingModule {

    @PerScreen
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun bindMain(): MainFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [AuthModule::class])
    fun bindAuth(): AuthFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [TeamModule::class])
    fun bindTeam(): TeamFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [PointsModule::class])
    fun bindPoints(): PointsFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [ResultModule::class])
    fun bindResult(): ResultFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun bindSettings(): SettingsFragment
}