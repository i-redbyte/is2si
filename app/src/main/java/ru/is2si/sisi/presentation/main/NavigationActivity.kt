package ru.is2si.sisi.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, NavigationActivity::class.java)
    }

}
