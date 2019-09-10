package ru.is2si.sisi.presentation.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_result.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.DelegationAdapter
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.model.CompetitionResultView
import ru.is2si.sisi.presentation.result.ResultDelegateTitle.Item
import javax.inject.Inject

class ResultFragment :
        ActionBarFragment<ResultContract.Presenter>(),
        NavigatorProvider,
        ResultContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher

    private lateinit var adapter: DelegationAdapter
    private lateinit var resultDelegate: ResultDelegate
    private lateinit var tableTitle: ResultDelegateTitle

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
        setupRecyclerView()
        setupView()
    }

    private fun setupView() {
        swipeRefreshContainer.setOnRefreshListener(this)
        swipeRefreshContainer.setColorSchemeResources(
                R.color.green,
                R.color.colorAccent,
                R.color.orange
        )
    }

    private fun setupRecyclerView() {
        adapter = DelegationAdapter()
        resultDelegate = ResultDelegate(requireContext()) {
            val result = adapter.items[it] as CompetitionResultView
            ResultDetailFragment.forProperties(result)
                    .showNow(childFragmentManager, null)
        }

        tableTitle = ResultDelegateTitle(requireContext())
        rvResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        adapter.delegatesManager
                .addDelegate(tableTitle)
                .addDelegate(resultDelegate)
        rvResult.adapter = adapter
    }

    override fun showCompetitionData(data: CompetitionResultView) {
        tvStartName.text = data.competition?.nameCompetition
    }

    override fun stopRefresh() {
        swipeRefreshContainer.isRefreshing = false
    }

    override fun onRefresh() {
        Log.d("_debug", "REFRESH")
        presenter.getResults()
    }

    override fun showResults(competitions: List<CompetitionResultView>) {
        val items = listOf(Item()) + competitions
        adapter.items = items
    }

    override fun showMain() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?, throwable: Throwable) =
            stateSwitcher.switchToError(message, throwable) { stateSwitcher.switchToMain() }

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