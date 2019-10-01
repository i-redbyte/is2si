package ru.is2si.sisi.presentation.points

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
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.presentation.model.PointView

class PointsDelegate(
        context: Context,
        private val pointClick: PointClickListener
) : AbsListItemAdapterDelegate<PointView, Any, PointsDelegate.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is PointView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_circle_point, parent, false)

        return Holder(view).apply {
            //ivClose.onClick { pointClick.onRemoveClick(adapterPosition) }
            itemView.onClick { pointClick.onPointClick(adapterPosition) }
        }
    }

    override fun onBindViewHolder(item: PointView, holder: Holder, payloads: List<Any>) =
            holder.bind(item)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPoint: TextView = requireViewById(itemView, R.id.tvPoint)
        //val ivClose: ImageView = requireViewById(itemView, R.id.ivClose)

        fun bind(item: PointView) {
            tvPoint.text = item.pointNameStr
        }
    }

}

interface PointClickListener {
    fun onRemoveClick(position: Int)
    fun onPointClick(position: Int)
}
