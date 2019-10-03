package ru.is2si.sisi.presentation.points.point

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_point.*
import ru.is2si.sisi.R
import ru.is2si.sisi.base.ActionBarFragment
import ru.is2si.sisi.base.extension.*
import ru.is2si.sisi.base.navigation.Navigator
import ru.is2si.sisi.base.navigation.NavigatorProvider
import ru.is2si.sisi.base.switcher.ViewStateSwitcher
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withCancelText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withCancelable
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withMessage
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withOkText
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.Companion.withTarget
import ru.is2si.sisi.presentation.files.FilesHandler
import ru.is2si.sisi.presentation.main.NavigationActivity
import ru.is2si.sisi.presentation.model.PointView
import java.io.IOException
import javax.inject.Inject

class PointFragment : ActionBarFragment<PointContract.Presenter>(),
        NavigatorProvider,
        PointContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    @Inject
    lateinit var filesHandler: FilesHandler

    private var photoPath = ""

    private val point: PointView
        get() = arguments?.getParcelable(ARG_POINT)
                ?: throw RuntimeException("Нет информации о точке") // TODO: Red_byte 2019-09-26 вынести в кастомный Exception

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_point, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.start()
    }

    private fun setupViews() {
        fabPhoto.onClick { checkPhotoPermission() }
    }

    private fun checkPhotoPermission() {
        when (beforeRequestPermissions(
                REQUEST_CAMERA,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )) {
            BeforeRequestPermissionResult.AlreadyGranted -> presenter.onCameraClick()
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_CAMERA,
                        true,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            BeforeRequestPermissionResult.Requested -> Unit
        }
    }

    override fun openCamera() {
        val ctx = requireContext()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(ctx.packageManager)?.also {
                try {
                    filesHandler.saveImage(requireContext()) { path, photoUri ->
                        photoPath = path
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(pictureIntent, REQUEST_CAMERA)
                    }
                } catch (err: IOException) {
                    showError(err.message, err)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (afterRequestPermissions(permissions, grantResults)) {
            AfterRequestPermissionsResult.Granted -> {
                when (requestCode) {
                    REQUEST_CAMERA -> presenter.onCameraClick()
                }
            }
            AfterRequestPermissionsResult.NeverAskAgain -> {
                AlertBottomSheetFragment()
                        .withMessage(getString(R.string.files_camera_requested))
                        .withOkText(getString(R.string.dialog_settings))
                        .withCancelText(getString(R.string.dialog_cancel))
                        .withCancelable(false)
                        .withTarget(this, REQUEST_CAMERA_PERMISSION)
                        .show(requireFragmentManager(), TAG_CAMERA_PERMISSION)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode == REQUEST_CAMERA_PERMISSION)) {
            if (AlertBottomSheetFragment.getResult(data) == AlertBottomSheetFragment.ControlResult.OK)
                startActivity(requireContext().appSettingsIntent())
        }
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQUEST_CAMERA -> {
                presenter.addToPhotosQueue(photoPath)
            }
        }
    }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        title = getString(R.string.point_title, point.pointNameStr)
        setDisplayHomeAsUpEnabled(true)
        setHomeButtonEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNavigator(): Navigator =
            (requireActivity() as NavigationActivity).getMainNavigator()

    override fun showMain() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?, throwable: Throwable) =
            stateSwitcher.switchToError(message, throwable) { stateSwitcher.switchToMain() }

    companion object {
        private const val ARG_POINT = "arg_point"
        private const val REQUEST_CAMERA = 517
        private const val REQUEST_CAMERA_PERMISSION = 1917
        private const val TAG_CAMERA_PERMISSION = "phone_permission"

        @JvmStatic
        fun forPoint(point: PointView) = PointFragment().withArguments {
            putParcelable(ARG_POINT, point)
        }
    }
}