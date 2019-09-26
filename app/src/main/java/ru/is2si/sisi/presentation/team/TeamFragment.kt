package ru.is2si.sisi.presentation.team

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_team.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.DebounceMenuSelectListener
import ru.is2si.sisi.base.MenuSelectCallback
import ru.is2si.sisi.base.extension.*
import ru.is2si.sisi.base.extension.AfterRequestPermissionsResult.Granted
import ru.is2si.sisi.base.extension.AfterRequestPermissionsResult.NeverAskAgain
import ru.is2si.sisi.base.extension.BeforeRequestPermissionResult.*
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.auth.AuthFragment
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withCancelText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withMessage
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withOkText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withTarget
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withTitle
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.ControlResult.OK
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.model.CompetitionResultView
import javax.inject.Inject

class TeamFragment :
    ActionBarFragment<TeamContract.Presenter>(),
    NavigatorProvider,
    MenuSelectCallback,
    TeamContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    private val menuSelectListener = DebounceMenuSelectListener(callBack = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_team, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PHONE_PERMISSION) {
            if (AlertBottomSheetFragment.getResult(data) == OK)
                startActivity(requireContext().appSettingsIntent())
        }
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQUEST_LOGOUT -> {
                if (AlertBottomSheetFragment.getResult(data) == OK)
                    presenter.logout()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) return
        requireFragmentManager().dismissDialogByTag(TAG_LOGOUT)
        requireFragmentManager().dismissDialogByTag(TAG_PHONE_PERMISSION)
    }

    private fun setupViews() {
        vPhone.onClick { checkPhonePermissions() }
    }

    override fun setTeam(team: CompetitionResultView) {
        tvPhoneNumber.text = team.competition?.emergencyPhone
        tvTeamName.text = team.team?.teamName ?: ""
        tvStartName.text = team.competition?.nameCompetition
        tvMaxNormalFinishTime.text = team.competition?.dataEndNorm?.getDateTimeOfPattern()
        tvFinishTimeLimit.text = team.competition?.dataEndMax?.getDateTimeOfPattern()
        tvPenaltyPoints.text = team.competition?.shtrafBallyNorma.toString()
        tvTechnicalDetails.text = team.competition?.organizingAuthority
    }

    override fun phoneCall() {
        val phone = tvPhoneNumber.text.toString()
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    private fun checkPhonePermissions() {
        when (beforeRequestPermissions(REQUEST_PHONE, CALL_PHONE)) {
            AlreadyGranted -> presenter.onPhoneClick()
            ShowRationale -> {
                beforeRequestPermissions(
                    REQUEST_PHONE,
                    true,
                    CALL_PHONE
                )
            }
            Requested -> Unit
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (afterRequestPermissions(permissions, grantResults)) {
            Granted -> {
                presenter.onPhoneClick()
            }
            NeverAskAgain -> {
                AlertBottomSheetFragment()
                    .withMessage(getString(R.string.team_phone_requested))
                    .withOkText(getString(R.string.dialog_settings))
                    .withCancelText(getString(R.string.dialog_cancel))
                    .withTarget(this, REQUEST_PHONE_PERMISSION)
                    .show(requireFragmentManager(), TAG_PHONE_PERMISSION)
            }
        }
    }

    override fun goToMain() {
        getNavigator().fragmentReplace(AuthFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        menuSelectListener.onSelected(item) or super.onOptionsItemSelected(item)

    override fun onMenuSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> {
            AlertBottomSheetFragment()
                .withTitle(getString(R.string.logout_title))
                .withMessage(getString(R.string.logout_text))
                .withOkText(getString(R.string.dialog_yes))
                .withCancelText(getString(R.string.dialog_no))
                .withTarget(this, REQUEST_LOGOUT)
                .show(requireFragmentManager(), TAG_LOGOUT)
            true
        }
        else -> false
    }

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?) = stateSwitcher.switchToError(message) {
        stateSwitcher.switchToMain()
    }

    override fun getNavigator(): Navigator =
        (requireActivity() as NavigationActivity).getMainNavigator()

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.team_title)
        setDisplayHomeAsUpEnabled(false)
    }

    companion object {
        private const val REQUEST_PHONE = 501
        private const val REQUEST_PHONE_PERMISSION = 3000
        private const val REQUEST_LOGOUT = 2000
        private const val TAG_PHONE_PERMISSION = "phone_permission"
        private const val TAG_LOGOUT = "logout"
        fun newInstance(): TeamFragment = TeamFragment()
    }
}