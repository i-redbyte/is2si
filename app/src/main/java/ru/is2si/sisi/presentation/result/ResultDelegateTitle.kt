package ru.is2si.sisi.presentation.result

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.is2si.sisi.R

class ResultDelegateTitle(context: Context) : AbsListItemAdapterDelegate<ResultDelegateTitle.Item, Any, ResultDelegateTitle.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is Item

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_result_title, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(item: Item, holder: Holder, payloads: List<Any>) =
            holder.bind(item)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item) = Unit
    }

    class Item
}