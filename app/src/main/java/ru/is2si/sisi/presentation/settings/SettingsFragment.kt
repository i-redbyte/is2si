package ru.is2si.sisi.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.setActionBar
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.presentation.main.NavigationActivity

class SettingsFragment : ActionBarFragment<SettingsContract.Presenter>(),
    NavigatorProvider,
    SettingsContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {

    }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.settings_title)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun getNavigator(): Navigator = (requireActivity() as NavigationActivity).getMainNavigator()

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}