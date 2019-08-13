package ru.is2si.sisi.presentation.main

import androidx.fragment.app.Fragment
import ru.is2si.sisi.base.navigation.TabContainerFragment
import ru.is2si.sisi.presentation.main.Tab.*
import ru.is2si.sisi.presentation.points.PointsFragment
import ru.is2si.sisi.presentation.result.ResultFragment
import ru.is2si.sisi.presentation.team.TeamFragment

class MainTabContainerFragment : TabContainerFragment() {

    override fun createTabFragment(tabName: String): Fragment = when (valueOf(tabName)) {
        TEAM -> TeamFragment.newInstance()
        POINTS -> PointsFragment.newInstance()
        FINISH -> Fragment()
        FILES -> Fragment()
        RESULT_TABLES -> ResultFragment.newInstance()
    }

    companion object {
        fun newInstance(): MainTabContainerFragment = MainTabContainerFragment()
    }
}

enum class Tab {
    TEAM,
    POINTS,
    FINISH,
    FILES,
    RESULT_TABLES,
}