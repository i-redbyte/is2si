package ru.is2si.sisi.presentation.points

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_points.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.DelegationAdapter
import ru.is2si.sisi.base.extension.getScreenWidth
import ru.is2si.sisi.base.extension.hideKeyboard
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withCancelText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withData
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withMessage
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withOkText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withTarget
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withTitle
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.ControlResult
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.model.PointView
import ru.is2si.sisi.presentation.points.point.PointFragment
import javax.inject.Inject

class PointsFragment :
    ActionBarFragment<PointsContract.Presenter>(),
    NavigatorProvider,
    PointsContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    private lateinit var adapter: DelegationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {
        setupRecyclerView()
        btnAdd.onClick {
            val name = tietPoint.text.toString()
            if (name.isNotEmpty()) {
                presenter.addPoint(name)
                tietPoint.text?.clear()
                requireActivity().hideKeyboard(tietPoint)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = DelegationAdapter()
        rvPoints.layoutManager = GridLayoutManager(requireContext(), getPointColumnCount())

        val pointClickListener = object : PointClickListener {
            override fun onRemoveClick(position: Int) {
                val point = adapter.items[position] as PointView
                showRemovePointDialog(point, position)
            }

            override fun onPointClick(position: Int) {
                val point = adapter.items[position] as PointView
                getNavigator().fragmentAdd(PointFragment.forPoint(point))
            }
        }

        val policyDelegate = PointsDelegate(requireContext(), pointClickListener)
        adapter.delegatesManager
            .addDelegate(policyDelegate)
        rvPoints.adapter = adapter
    }

    private fun getPointColumnCount(): Int {
        val width = requireContext().getScreenWidth()
        val pointSize = requireContext().resources.getDimension(R.dimen.point_circle_size)
        var numberOfColumns = (width / pointSize).toInt()
        val remaining = width - (numberOfColumns * width)
        if (remaining / (2 * numberOfColumns) < 15)
            numberOfColumns--
        return numberOfColumns
    }

    private fun showRemovePointDialog(point: PointView, position: Int) {
        AlertBottomSheetFragment()
            .withTitle(getString(R.string.points_remove_point))
            .withMessage(getString(R.string.points_remove_point_message))
            .withOkText(getString(R.string.dialog_yes))
            .withCancelText(getString(R.string.dialog_no))
            .withTarget(this, REQUEST_REMOVE_POINT)
            .withData {
                putParcelable(ARG_POINT, point)
                putInt(ARG_POINT_POSITION, position)
            }
            .show(requireFragmentManager(), TAG_REMOVE_POINT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQUEST_REMOVE_POINT -> {
                when (AlertBottomSheetFragment.getResult(data)) {
                    ControlResult.OK -> {
                        val point: PointView = data?.getParcelableExtra(ARG_POINT)
                            ?: return
                        val position: Int = data.getIntExtra(ARG_POINT_POSITION, -1)
                        if (position == -1) return
                        presenter.removePoint(point, position)
                    }
                    ControlResult.CANCEL -> Unit
                }
            }
        }
    }

    override fun showPoints(points: List<PointView>) {
        adapter.items = points
        showSummaryBalls(points)
    }

    override fun showPointsByRemove(position: Int) {
        adapter.notifyItemRemoved(position)
        showSummaryBalls(adapter.items.map { it as PointView })
    }

    override fun showSummaryBalls(points: List<PointView>) {
        val sum = points.sumBy { it.pointBall }
        tvSummary.text = sum.toString()
    }

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?) = stateSwitcher.switchToError(message) {
        stateSwitcher.switchToMain()
    }

    override fun showMain() =
        stateSwitcher.switchToMain()

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.points_title)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            requireActivity().hideKeyboard(tietPoint)
    }

    override fun getNavigator(): Navigator =
        (requireActivity() as NavigationActivity).getMainNavigator()

    override fun showSnack(message: String?) {
        Snackbar.make(requireView(), message as CharSequence, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val REQUEST_REMOVE_POINT = 2100
        private const val TAG_REMOVE_POINT = "remove_point"
        private const val ARG_POINT = "point"
        private const val ARG_POINT_POSITION = "point_position"
        @JvmStatic
        fun newInstance(): PointsFragment = PointsFragment()
    }


}