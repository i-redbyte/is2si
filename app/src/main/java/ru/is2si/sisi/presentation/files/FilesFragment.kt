package ru.is2si.sisi.presentation.files

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_files.*
import ru.is2si.sisi.BuildConfig
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
import ru.is2si.sisi.presentation.design.dialog.AlertBottomSheetFragment.ControlResult.OK
import ru.is2si.sisi.presentation.main.NavigationActivity
import java.io.IOException
import javax.inject.Inject

class FilesFragment : ActionBarFragment<FilesContract.Presenter>(),
        NavigatorProvider, FilesContract.View {

    @Inject
    lateinit var stateSwitcher: ViewStateSwitcher
    private var photoPath = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_files, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
        setupView()
    }

    private fun setupView() {
        btnUpload.onClick {
            presenter.uploadFiles()
        }

        fabPhoto.onClick {
            checkPhotoPermission()
        }
    }

    override fun openCamera() {
        val ctx = requireContext()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(ctx.packageManager)?.also {
                val photoFile = try {
                    presenter.createPhoto(
                            ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                    ?: throw IOException()
                    )
                } catch (err: IOException) {
                    showError(err.message, err)
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            ctx,
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            it
                    )
                    photoPath = photoFile.absolutePath
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(pictureIntent, REQUEST_CAMERA)
                }
            }
        }
    }

    private fun checkPhotoPermission() {
        when (beforeRequestPermissions(REQUEST_CAMERA, CAMERA, WRITE_EXTERNAL_STORAGE)) {
            BeforeRequestPermissionResult.AlreadyGranted -> presenter.onCameraClick()
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_CAMERA,
                        true,
                        CAMERA, WRITE_EXTERNAL_STORAGE
                )
            }
            BeforeRequestPermissionResult.Requested -> Unit
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (afterRequestPermissions(permissions, grantResults)) {
            AfterRequestPermissionsResult.Granted -> {
                presenter.onCameraClick()
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
        if (resultCode == RESULT_OK && (requestCode == REQUEST_CAMERA_PERMISSION)) {
            if (AlertBottomSheetFragment.getResult(data) == OK)
                startActivity(requireContext().appSettingsIntent())
        }
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            REQUEST_CAMERA -> {
                presenter.addToPhotosQueue(photoPath)
            }
        }

    }

    override fun showSuccessUpload() {
        Snackbar.make(fabPhoto, getString(R.string.files_success_upload),Snackbar.LENGTH_LONG).show()
    }

    override fun showMain() = stateSwitcher.switchToMain()

    override fun showLoading() = stateSwitcher.switchToLoading()

    override fun showError(message: String?, throwable: Throwable) =
            stateSwitcher.switchToError(message, throwable) { stateSwitcher.switchToMain() }

    override fun showError(message: String) =
            stateSwitcher.switchToError(message) { stateSwitcher.switchToMain() }

    override fun findToolbar(): Toolbar? = view?.findViewById(R.id.tActionBar)

    override fun setupActionBar() = setActionBar(findToolbar()) {
        setTitle(R.string.finish_title)
        setDisplayHomeAsUpEnabled(false)
    }

    override fun getNavigator(): Navigator =
            (requireActivity() as NavigationActivity).getMainNavigator()

    companion object {
        private const val REQUEST_CAMERA = 517
        private const val REQUEST_CAMERA_PERMISSION = 1917
        private const val TAG_CAMERA_PERMISSION = "phone_permission"
        fun newInstance(): FilesFragment = FilesFragment()
    }
}