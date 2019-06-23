package ru.is2si.sisi.presentation.design.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.is2si.sisi.R
import ru.is2si.sisi.base.AdapterSelectManager
import ru.is2si.sisi.base.DelegationAdapter
import ru.is2si.sisi.base.ItemClickListener
import ru.is2si.sisi.base.extension.onClick


class RadioItemDelegate(
    context: Context,
    private val selectManager: AdapterSelectManager,
    private val itemClickListener: ItemClickListener
) : AbsListItemAdapterDelegate<String, Any, RadioItemDelegate.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var blockSelection = false

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is String

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_radio_selection, parent, false)
        return Holder(view).apply {
            rbItem.onClick {
                if (blockSelection)
                    return@onClick
                blockSelection = true
                selectManager.selectedItem = item
                rbItem.postDelayed({ itemClickListener.invoke(adapterPosition) }, 150)
            }
        }
    }

    override fun onBindViewHolder(
            item: String,
            holder: Holder,
            payloads: List<Any>
    ) = with(holder) {
        this.item = item
        if (payloads.size == 1 && payloads.contains(DelegationAdapter.Payload.UPDATE)) {
            showChecked(item, selectManager)
            return@with
        }
        rbItem.text = item
        showChecked(item, selectManager)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rbItem: RadioButton = itemView as RadioButton

        lateinit var item: String

        fun showChecked(item: String, selectManager: AdapterSelectManager) {
            rbItem.isChecked = item == selectManager.selectedItem
        }
    }
}
