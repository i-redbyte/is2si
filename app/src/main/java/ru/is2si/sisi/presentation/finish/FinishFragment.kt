package ru.is2si.sisi.presentation.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_finish.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.main.NavigationActivity
import javax.inject.Inject

class FinishFragment : ActionBarFragment<FinishContract.Presenter>(),
        NavigatorProvider, FinishContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_finish, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
        setupView()
    }

    private fun setupView() {
        btnFinish.onClick { presenter.finishTeam() }
    }

    override fun showFixFinishTime(fixFinishTime: String) {
        tvFixedTime.text = fixFinishTime
    }

    override fun showFinishData(
            maxNormalTime: String,
            amountPenaltyPoints: String,
            preliminaryPoints: String,
            dateTimeFinish: String
    ) {
        tvMaxTime.text = maxNormalTime
        tvAmountPenaltyPoints.text = amountPenaltyPoints
        tvPreliminaryPoints.text = preliminaryPoints
        tvFixedTime.text = dateTimeFinish
    }

    override fun showMain() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?, throwable: Throwable) =
            stateSwitcher.switchToError(message, throwable) { stateSwitcher.switchToMain() }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.finish_title)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun getNavigator(): Navigator = (requireActivity() as NavigationActivity).getMainNavigator()

    companion object {
        fun newInstance(): FinishFragment = FinishFragment()
    }
}