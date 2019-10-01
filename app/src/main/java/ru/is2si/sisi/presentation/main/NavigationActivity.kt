package ru.is2si.sisi.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import ru.is2si.sisi.R
import ru.is2si.sisi.base.extension.commitTransactionNow
import ru.is2si.sisi.base.navigation.BackButtonListener
import ru.is2si.sisi.presentation.auth.AuthFragment

class NavigationActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout
    private lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSoftInputMode()
        frameLayout = FrameLayout(this)
        frameLayout.id = R.id.vgContainer
        setContentView(frameLayout)
        if (savedInstanceState == null) {
            supportFragmentManager.commitTransactionNow {
                replace(frameLayout.id, AuthFragment.newInstance())
            }
        }
        navigator = MainNavigator(supportFragmentManager, R.id.vgContainer)
    }

    fun getMainNavigator(): MainNavigator = navigator

    override fun onBackPressed() {
        val current = supportFragmentManager.fragments
                .lastOrNull { it.isVisible }
                as? BackButtonListener
        if (current?.onBackPressed() == true)
            return
        super.onBackPressed()
    }

    private fun setSoftInputMode() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val density = metrics.densityDpi
        if (density <= DisplayMetrics.DENSITY_HIGH) {
            with(window) {
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent = Intent(context, NavigationActivity::class.java)
    }

}
