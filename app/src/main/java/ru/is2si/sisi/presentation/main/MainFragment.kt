package ru.is2si.sisi.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.R
import ru.is2si.sisi.base.BaseFragment
import ru.is2si.sisi.base.extension.commitTransactionNow
import ru.is2si.sisi.base.extension.compatDrawable
import ru.is2si.sisi.base.navigation.BackButtonListener
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.navigation.TabContainerFragment
import ru.is2si.sisi.base.navigation.TabContainerFragment.Companion.withTab
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.main.Tab.*
import java.util.*
import javax.inject.Inject

class MainFragment :
        BaseFragment<MainContract.Presenter>(),
        NavigatorProvider,
        MainContract.View,
        BackButtonListener {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher

    private lateinit var mainNavigationMenu: MainNavigationMenu
    private val selectedTabSequence: MutableList<Tab> = LinkedList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        if (savedInstanceState == null) {
            val tab = TEAM
            mainNavigationMenu.setSelectedItemId(tab)
            showTab(tab)
        } else {
            val sequence = savedInstanceState.getStringArrayList(ARG_SELECTED_TAB_SEQUENCE)
                    ?: listOf<String>()
            val tabSequence = sequence.map { valueOf(it) }
            selectedTabSequence.clear()
            selectedTabSequence.addAll(tabSequence)
        }

        presenter.start()
    }

    private fun setupNavigation() {
        mainNavigationMenu = MainNavigationMenu(requireContext(), newMenu())
        mainNavigationMenu.setOnItemSelectedCallback {
            showTab(it)
            true
        }
        mainNavigationMenu.setOnItemReselectedCallback(::showRootForTab)
    }

    private inline fun selectRootTabAndNavigate(tab: Tab, crossinline action: Navigator.() -> Unit) {
        mainNavigationMenu.setSelectedItemId(tab)
        showTab(tab)
        showRootForTab(tab)
        view?.post {
            val tabNavigator = getTabNavigator(tab) ?: return@post
            action.invoke(tabNavigator)
        }
    }

    private fun getTabNavigator(tab: Tab): Navigator? {
        val current = childFragmentManager.fragments
                .filterIsInstance<MainTabContainerFragment>()
                .firstOrNull { it.tab == tab }
                ?: return null
        val fragment = current.childFragmentManager.fragments
                .firstOrNull()
                ?: return null
        return (fragment as? NavigatorProvider)?.getNavigator()
    }

    override fun showError(throwable: Throwable) = stateSwitcher.switchToError(throwable.message) {}

    override fun showContent() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun getNavigator(): Navigator = (parentFragment as NavigatorProvider).getNavigator()

    private fun showTab(tab: Tab) {
        var current = childFragmentManager.fragments
                .filterIsInstance<TabContainerFragment>()
                .firstOrNull { it.tab == tab }
        childFragmentManager.commitTransactionNow {
            if (current == null) {
                current = MainTabContainerFragment.newInstance()
                        .withTab(tab.name)
                add(R.id.vgContainer, current!!)
            } else {
                show(current!!)
            }
            val requireHide = childFragmentManager.fragments
                    .toMutableList()
                    .apply { remove(current) }
            requireHide.forEach { hide(it) }
        }
        addTabToSequence(tab)
    }

    private fun addTabToSequence(tab: Tab) {
        selectedTabSequence.remove(tab)
        selectedTabSequence.add(tab)
    }

    private fun showRootForTab(tab: Tab) {
        val current = childFragmentManager.fragments
                .filterIsInstance<TabContainerFragment>()
                .firstOrNull { it.tab == tab }
                ?: return
        val childFragmentManager = current.childFragmentManager
        val backStackEntryCount = childFragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount)
            childFragmentManager.popBackStackImmediate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val selectedTabSequence = ArrayList(selectedTabSequence.map(Tab::name))
        outState.putStringArrayList(ARG_SELECTED_TAB_SEQUENCE, selectedTabSequence)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed(): Boolean {
        val current = childFragmentManager.fragments
                .filterIsInstance<MainTabContainerFragment>()
                .firstOrNull { it.isVisible }
        return if (current == null) {
            false
        } else {
            if (current.childFragmentManager.backStackEntryCount > 0) {
                current.onBackPressed()
            } else {
                val previousTab = selectedTabSequence
                        .dropLast(1)
                        .lastOrNull()
                val previous = childFragmentManager.fragments
                        .filterIsInstance<MainTabContainerFragment>()
                        .firstOrNull { it.isHidden && it.tab == previousTab }
                if (previous == null) {
                    false
                } else {
                    selectedTabSequence.remove(selectedTabSequence.lastOrNull())
                    val tab = previous.tab
                    mainNavigationMenu.setSelectedItemId(tab)
                    showTab(tab)
                    true
                }
            }
        }
    }

    companion object {
        private const val ARG_SELECTED_TAB_SEQUENCE = "$APPLICATION_ID.arg.SELECTED_TAB_SEQUENCE"

        fun newInstance(): MainFragment = MainFragment()
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun MainFragment.newMenu(): List<NavigationItem> = listOf(
        NavigationItem(
                TEAM,
                tvTeam,
                compatDrawable(R.drawable.ic_menu_team),
                getString(R.string.main_menu_team)
        ),
        NavigationItem(
                POINTS,
                tvPoints,
                compatDrawable(R.drawable.ic_menu_points),
                getString(R.string.main_menu_points)
        ),
        NavigationItem(
                FINISH,
                tvFinish,
                compatDrawable(R.drawable.ic_menu_finish),
                getString(R.string.main_menu_finish)
        ),
        NavigationItem(
                FILES,
                tvFiles,
                compatDrawable(R.drawable.ic_menu_files),
                getString(R.string.main_menu_files)
        ),
        NavigationItem(
                RESULT_TABLES,
                tvTableResults,
                compatDrawable(R.drawable.ic_menu_table_results),
                getString(R.string.main_menu_table_results)
        )
)

private val TabContainerFragment.tab: Tab get() = valueOf(tabName)