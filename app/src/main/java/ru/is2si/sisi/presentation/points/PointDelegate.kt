package ru.is2si.sisi.presentation.points

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ItemClickListener
import ru.is2si.sisi.base.extension.onClick
import ru.is2si.sisi.presentation.model.PointView

class PointDelegate(
        context: Context,
        private val itemClick: ItemClickListener
) : AbsListItemAdapterDelegate<PointView, Any, PointDelegate.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is PointView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_point, parent, false)

        return Holder(view).apply {
            ivClose.onClick { itemClick.invoke(adapterPosition) }
        }
    }

    override fun onBindViewHolder(item: PointView, holder: Holder, payloads: List<Any>) =
            holder.bind(item)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPointName: TextView = requireViewById(itemView, R.id.tvPointName)
        private val tvPointValue: TextView = requireViewById(itemView, R.id.tvPointValue)
        val ivClose: ImageView = requireViewById(itemView, R.id.ivClose)

        fun bind(item: PointView) {
            tvPointName.text = item.pointNameStr
            tvPointValue.text = item.pointBall.toString()
        }
    }

}
