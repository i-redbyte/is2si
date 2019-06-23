package ru.is2si.sisi.base.navigation

import androidx.fragment.app.Fragment

interface Navigator {

    fun fragmentReplace(fragment: Fragment)

    fun fragmentAdd(fragment: Fragment)

}