package ru.is2si.sisi.base.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.is2si.sisi.R
import ru.is2si.sisi.base.extension.commitTransactionNow
import ru.is2si.sisi.base.extension.withArguments
import ru.is2si.sisi.presentation.main.TabNavigator

abstract class TabContainerFragment : Fragment(), NavigatorProvider, BackButtonListener {

    private val tabNavigator: Navigator
            by lazy { TabNavigator(childFragmentManager, R.id.vgTabContainer) }

    val tabName: String get() = arguments?.getString(ARG_TAB) ?: ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tab_container, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.vgTabContainer) == null) {
            val fragment = createTabFragment(tabName)
            tabNavigator.fragmentReplace(fragment)
        }
    }

    protected abstract fun createTabFragment(tabName: String): Fragment

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        val currentFragment = childFragmentManager.findFragmentById(R.id.vgTabContainer) ?: return
        childFragmentManager.commitTransactionNow {
            if (hidden) hide(currentFragment) else show(currentFragment)
        }
    }

    override fun getNavigator(): Navigator = tabNavigator

    override fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }

    companion object {
        private const val ARG_TAB = "tab"

        @JvmStatic
        fun <T : TabContainerFragment> T.withTab(tabName: String): T = withArguments {
            putString(ARG_TAB, tabName)
        }
    }

}