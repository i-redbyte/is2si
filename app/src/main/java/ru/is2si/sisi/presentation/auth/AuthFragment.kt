package ru.is2si.sisi.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.FragmentNavigator
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.team.TeamFragment
import javax.inject.Inject

class AuthFragment :
        ActionBarFragment<AuthContract.Presenter>(),
        AuthContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    @Inject
    lateinit var navigator: FragmentNavigator

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
        navigator.fragmentAdd(TeamFragment.newInstance())
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

    companion object {
        fun newInstance(): AuthFragment = AuthFragment()
    }
}