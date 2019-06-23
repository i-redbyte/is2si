package ru.is2si.sisi.base

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

typealias ItemClickListener = (position: Int) -> Unit

class DelegationAdapter : ListDelegationAdapter<List<Any>>(AdapterDelegatesManager()) {

    val delegatesManager: AdapterDelegatesManager<List<Any>>
        get() = super.delegatesManager

    override fun setItems(items: List<Any>) {
        val oldSize = this.items?.size ?: 0
        super.setItems(listOf())
        if (oldSize != 0)
            notifyItemRangeRemoved(0, oldSize)
        super.setItems(items)
        notifyItemRangeInserted(0, items.size)
    }

}