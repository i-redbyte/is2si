package ru.is2si.sisi.presentation.points.point

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.extension.withArguments
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.model.PointView
import javax.inject.Inject

class PointFragment : ActionBarFragment<PointContract.Presenter>(),
        NavigatorProvider,
        PointContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher

    private val point: PointView
        get() = arguments?.getParcelable(ARG_POINT)
                ?: throw RuntimeException("Нет информации о точке") // TODO: Red_byte 2019-09-26 вынести в кастомный Exception

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_point, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {

    }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        title = getString(R.string.point_title, point.pointNameStr)
        setDisplayHomeAsUpEnabled(true)
        setHomeButtonEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    override fun getNavigator(): Navigator =
            (requireActivity() as NavigationActivity).getMainNavigator()

    override fun showMain() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?, throwable: Throwable) =
            stateSwitcher.switchToError(message, throwable) { stateSwitcher.switchToMain() }

    companion object {
        const val ARG_POINT = "arg_point"
        fun forPoint(point: PointView) = PointFragment().withArguments {
            putParcelable(ARG_POINT, point)
        }
    }
}