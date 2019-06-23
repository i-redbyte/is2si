package ru.is2si.sisi

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import ru.is2si.sisi.base.extension.commitTransactionNow
import ru.is2si.sisi.base.navigation.FragmentNavigator
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.presentation.auth.AuthFragment

class MainActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

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
    }
}
