package ru.is2si.sisi.base

import ru.is2si.sisi.base.DelegationAdapter.Payload.*

class AdapterMultiSelectManager(private val adapter: DelegationAdapter) {

    private val _selectedItems: MutableList<Any> = mutableListOf()

    fun selectItem(item: Any, select: Boolean) {
        if (select) _selectedItems.add(item) else _selectedItems.remove(item)
        val indexOfItem = adapter.items
                .indexOfFirst { it == item }
        if (indexOfItem != -1)
            adapter.notifyItemChanged(indexOfItem, UPDATE)
    }

    fun isItemSelected(item: Any) = _selectedItems.contains(item)

    fun getSelectedItems(): List<Any> = _selectedItems.toList()

}