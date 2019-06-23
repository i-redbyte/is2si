package ru.is2si.sisi.base

class AdapterSelectManager(private val adapter: DelegationAdapter) {

    var selectedItem: Any? = null
        set(value) {
            val indexOfLastSelected = adapter.items
                    .indexOfFirst { it == field }
            val indexOfSelected = adapter.items
                    .indexOfFirst { it == value }
            field = value
            if (indexOfLastSelected != -1)
                adapter.notifyItemChanged(indexOfLastSelected, DelegationAdapter.Payload.UPDATE)
            if (indexOfSelected != -1)
                adapter.notifyItemChanged(indexOfSelected, DelegationAdapter.Payload.UPDATE)

        }

}