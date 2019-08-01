package ru.is2si.sisi.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_result.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.presentation.main.NavigationActivity
import javax.inject.Inject

class ResultFragment :
        ActionBarFragment<ResultContract.Presenter>(),
        NavigatorProvider,
        ResultContract.View {

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
    ): View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {
        btnGood.onClick { presenter.getResult() }
    }

    override fun showResult(competitions: List<CompetitionResult>) {
        Toast
                .makeText(
                        requireContext(),
                        "Получили список из ${competitions.size} элементов",
                        Toast.LENGTH_LONG
                )
                .show()
        if (competitions.isNotEmpty()) {
            val first = competitions.first()
            Toast
                    .makeText(
                            requireContext(),
                            "Первый эл-т: id = ${first.id}, name= ${first.idCompetition.nameCompetition}",
                            Toast.LENGTH_LONG
                    )
                    .show()

        }
    }

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?) = stateSwitcher.switchToError(message) {
        stateSwitcher.switchToMain()
    }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.result_title)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun getNavigator(): Navigator = (requireActivity() as NavigationActivity).getMainNavigator()

    companion object {
        fun newInstance(): ResultFragment = ResultFragment()
    }
}