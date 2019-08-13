package ru.is2si.sisi.presentation.auth

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.DebounceMenuSelectListener
import ru.is2si.sisi.base.MenuSelectCallback
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.main.MainFragment
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.settings.SettingsFragment
import javax.inject.Inject

class AuthFragment :
        ActionBarFragment<AuthContract.Presenter>(),
        NavigatorProvider,
        AuthContract.View,
        MenuSelectCallback {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    private val menuSelectListener = DebounceMenuSelectListener(callBack = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {
        btnComeIn.onClick {
            presenter.authForPinCode(tietPinCode.text.toString())
        }
    }

    override fun gotoTeamScreen() {
        getNavigator().fragmentReplace(MainFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            menuSelectListener.onSelected(item) or super.onOptionsItemSelected(item)

    override fun onMenuSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            getNavigator().fragmentAdd(SettingsFragment.newInstance())
            true
        }
        else -> false
    }

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?) = stateSwitcher.switchToError(message) {
        stateSwitcher.switchToMain()
    }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.app_name)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun getNavigator(): Navigator = (requireActivity() as NavigationActivity).getMainNavigator()

    companion object {
        fun newInstance(): AuthFragment = AuthFragment()
    }
}