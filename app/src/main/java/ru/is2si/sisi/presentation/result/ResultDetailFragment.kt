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

    // FIXME: 2019-09-10 Убрать строки в ресурсы
    private fun setupViews() {
        tvTeam.text = "Команда: ${result.team?.teamName}"
        tvGroup.text = "Группа: ${result.disciplina?.discipline?.groupName ?: "-"}"
        tvPlace.text = "Место команды в старте: ${result.placeEntry}"
        tvPoints.text = "Баллы: ${result.bally}"
        tvPenaltyPoints.text = "Штрафные баллы: ${result.penaltyBally}"
        tvFinalPoints.text = "Финальные результаты: ${result.resultBally}"
        tvFinishTime.text = "Время финиша: ${result.dataTimeFinish?.getDateTimeOfPattern()?:"-"}"
        tvDistanceTime.text = "Время на дистанции: ${result.dataTimeFinish?.getDateTimeOfPattern() ?: "-"}"
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
