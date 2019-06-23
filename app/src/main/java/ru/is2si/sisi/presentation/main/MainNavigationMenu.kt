package ru.is2si.sisi.presentation.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import ru.is2si.sisi.R
import ru.is2si.sisi.base.extension.compatColor

class MainNavigationMenu(
        context: Context,
        private val navigationItems: List<NavigationItem>
) {

    private val inactiveColor: Int = context.compatColor(R.color.silver_chalice)
    private val activeColor: Int = context.compatColor(R.color.green)

    private var onItemSelectedCallback: OnItemSelectedCallback? = null
    private var onItemReselectedCallback: OnItemReselectedCallback? = null

    var selectedItem: Tab? = null
        private set

    init {
        navigationItems
                .forEach { navigationItem ->
                    navigationItem.itemView.text = navigationItem.text
                    navigationItem.itemView.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            navigationItem.icon,
                            null,
                            null
                    )
                    navigationItem.itemView.setOnClickListener { selectItem(navigationItem.tab) }
                    setItemActive(navigationItem.itemView, false)
                }
    }

    private fun selectItem(tab: Tab) {
        if (selectedItem == tab) {
            onItemReselectedCallback?.onItemReselected(tab)
            return
        }
        if (onItemSelectedCallback?.onItemSelected(tab) == true) {
            selectedItem = tab
            invalidateMenu()
        }
    }

    private fun setItemActive(itemView: TextView, active: Boolean) {
        val icon = itemView.compoundDrawables[1]
        if (active) {
            DrawableCompat.setTint(icon, activeColor)
            itemView.setTextColor(activeColor)
        } else {
            DrawableCompat.setTint(icon, inactiveColor)
            itemView.setTextColor(inactiveColor)
        }
    }

    fun setSelectedItemId(tab: Tab) {
        selectedItem = tab
        invalidateMenu()
    }

    private fun invalidateMenu() {
        navigationItems.forEach { setItemActive(it.itemView, it.tab == selectedItem) }
    }

    inline fun setOnItemSelectedCallback(crossinline callback: (tab: Tab) -> Boolean) {
        setOnItemSelectedCallback(object : OnItemSelectedCallback {
            override fun onItemSelected(tab: Tab): Boolean = callback.invoke(tab)
        })
    }

    fun setOnItemSelectedCallback(onItemSelectedCallback: OnItemSelectedCallback) {
        this.onItemSelectedCallback = onItemSelectedCallback
    }

    inline fun setOnItemReselectedCallback(crossinline callback: (tab: Tab) -> Unit) {
        setOnItemReselectedCallback(object : OnItemReselectedCallback {
            override fun onItemReselected(tab: Tab) = callback.invoke(tab)
        })
    }

    fun setOnItemReselectedCallback(onItemReselectedCallback: OnItemReselectedCallback) {
        this.onItemReselectedCallback = onItemReselectedCallback
    }

}

interface OnItemSelectedCallback {
    fun onItemSelected(tab: Tab): Boolean
}

interface OnItemReselectedCallback {
    fun onItemReselected(tab: Tab)
}

class NavigationItem(
        val tab: Tab,
        val itemView: TextView,
        val icon: Drawable?,
        val text: String
)