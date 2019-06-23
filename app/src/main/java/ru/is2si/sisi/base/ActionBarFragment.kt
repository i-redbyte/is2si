package ru.is2si.sisi.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar

abstract class ActionBarFragment<P : BaseContract.Presenter> : BaseFragment<P>() {

    protected abstract fun findToolbar(): Toolbar?

    protected abstract fun setupActionBar()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isHidden.not()) setupActionBar()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) setupActionBar()
    }

}