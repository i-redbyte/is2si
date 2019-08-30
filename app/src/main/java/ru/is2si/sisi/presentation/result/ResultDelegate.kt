package ru.is2si.sisi.presentation.result

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ItemClickListener
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.presentation.model.CompetitionResultView

class ResultDelegate(
        context: Context,
        private val itemClick: ItemClickListener
) : AbsListItemAdapterDelegate<CompetitionResultView, Any, ResultDelegate.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is CompetitionResultView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_result, parent, false)
        return Holder(view).apply {
            itemView.onClick { itemClick.invoke(adapterPosition) }
        }
    }

    override fun onBindViewHolder(item: CompetitionResultView, holder: Holder, payloads: List<Any>) =
            holder.bind(item)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTeam: TextView = requireViewById(itemView, R.id.tvTeam)
        private val tvGroup: TextView = requireViewById(itemView, R.id.tvGroup)
        private val tvPlace: TextView = requireViewById(itemView, R.id.tvPlace)

        fun bind(item: CompetitionResultView) {
            tvTeam.text = item.team?.teamName
            tvGroup.text = item.group
            tvPlace.text = item.placeEntry.toString()
        }
    }

}