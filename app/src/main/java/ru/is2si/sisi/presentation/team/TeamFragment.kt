package ru.is2si.sisi.presentation.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.is2si.sisi.R
import ru.is2si.sisi.base.BaseFragment
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import javax.inject.Inject

class TeamFragment :
        BaseFragment<TeamContract.Presenter>(),
        TeamContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_team, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {

    }

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?) = stateSwitcher.switchToError(message){
        stateSwitcher.switchToMain()
    }

    companion object {
        fun newInstance(): TeamFragment = TeamFragment()
    }
}