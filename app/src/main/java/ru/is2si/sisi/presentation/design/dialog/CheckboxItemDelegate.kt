package ru.is2si.sisi.presentation.design.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.is2si.sisi.R
import ru.is2si.sisi.base.AdapterMultiSelectManager
import ru.is2si.sisi.base.DelegationAdapter

class CheckboxItemDelegate(
        context: Context,
        private val multiSelectManager: AdapterMultiSelectManager
) : AbsListItemAdapterDelegate<AlertBottomSheetFragment.MultiItem<*>, Any, CheckboxItemDelegate.Holder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is AlertBottomSheetFragment.MultiItem<*>

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_checkbox_selection, parent, false)
        return Holder(view).apply {
            itemView.setOnClickListener {
                multiSelectManager.selectItem(item, cbSelection.isSelected.not())
            }
        }
    }

    override fun onBindViewHolder(
        item: AlertBottomSheetFragment.MultiItem<*>,
        holder: Holder,
        payloads: List<Any>
    ) = with(holder) {
        this.item = item
        if (payloads.size == 1 && payloads.contains(DelegationAdapter.Payload.UPDATE)) {
            showChecked(item, multiSelectManager)
            return@with
        }
        tvTitle.text = item.title
        tvCount.text = item.checkTitle
        showChecked(item, multiSelectManager)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = requireViewById(itemView, R.id.tvTitle)
        val tvCount: TextView = requireViewById(itemView, R.id.tvCount)
        val cbSelection: ImageView = requireViewById(itemView, R.id.cbSelection)

        lateinit var item: AlertBottomSheetFragment.MultiItem<*>

        fun showChecked(item: AlertBottomSheetFragment.MultiItem<*>, multiSelectManager: AdapterMultiSelectManager) {
            cbSelection.isSelected = multiSelectManager.isItemSelected(item)
        }
    }

}