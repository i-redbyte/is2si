package ru.is2si.sisi.presentation.result

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_team_detail.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.extension.getDateTimeOfPattern
import ru.is2si.sisi.base.extension.withArguments
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment
import ru.is2si.sisi.presentation.model.CompetitionResultView

class ResultDetailFragment : AlertBottomSheetFragment() {

    override val layoutId: Int = R.layout.fragment_team_detail

    private val result: CompetitionResultView
        get() = arguments?.getParcelable(ARG_TEAM)
                ?: throw RuntimeException("Нет информации о команде") // TODO: Red_byte 2019-08-31 вынести в кастомный Exception

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        tvTeam.text = result.team?.teamName
        tvGroup.text = result.group
        tvPlace.text = result.placeEntry.toString()
        tvPoints.text = result.bally
        tvPenaltyPoints.text = result.penaltyBally
        tvFinalPoints.text = result.resultBally
        tvFinishTime.text = result.dataTimeFinish?.getDateTimeOfPattern()
        tvDistanceTime.text = "Время на дистанции: ${result.dataTimeFinish?.getDateTimeOfPattern()}"
    }

    companion object {
        private const val ARG_TEAM = "team_info"

        @JvmStatic
        fun forProperties(result: CompetitionResultView): ResultDetailFragment {
            return ResultDetailFragment()
                    .withArguments { putParcelable(ARG_TEAM, result) }
        }
    }

}
