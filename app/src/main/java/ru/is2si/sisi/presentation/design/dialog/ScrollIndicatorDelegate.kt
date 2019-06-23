package ru.is2si.sisi.presentation.design.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.is2si.sisi.R
import ru.is2si.sisi.presentation.design.dialog.ScrollIndicatorDelegate.*


class ScrollIndicatorDelegate(
        context: Context
) : AbsListItemAdapterDelegate<Item, Any, Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is Item

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_scroll_indicator, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(
            item: Item,
            holder: Holder,
            payloads: List<Any>
    ) = Unit

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class Item

}
