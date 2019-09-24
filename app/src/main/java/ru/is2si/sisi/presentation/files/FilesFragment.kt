package ru.is2si.sisi.presentation.files

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_files.*
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

    @Inject
    lateinit var filesHandler: FilesHandler

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
        btnUploadTrack.onClick {
            checkStoragePermission()
        }
        btnUploadPhotos.onClick { presenter.uploadFiles() }
        fabPhoto.onClick { checkPhotoPermission() }
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

    private fun selectTrack() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val mimeTypes = arrayOf("text/*", "image/*", "application/*")
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_TRACK)
    }

    private fun checkPhotoPermission() {
        when (beforeRequestPermissions(
                REQUEST_CAMERA,
                CAMERA,
                WRITE_EXTERNAL_STORAGE,
                ACCESS_COARSE_LOCATION
        )) {
            BeforeRequestPermissionResult.AlreadyGranted -> presenter.onCameraClick()
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_CAMERA,
                        true,
                        CAMERA, WRITE_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION
                )
            }
            BeforeRequestPermissionResult.Requested -> Unit
        }
    }

    private fun checkStoragePermission() {
        when (beforeRequestPermissions(
                REQUEST_STORAGE_PERMISSION,
                WRITE_EXTERNAL_STORAGE,
                ACCESS_COARSE_LOCATION,
                CAMERA
        )) {
            BeforeRequestPermissionResult.AlreadyGranted -> selectTrack()
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_STORAGE_PERMISSION,
                        true,
                        CAMERA, WRITE_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION
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
                when (requestCode) {
                    REQUEST_STORAGE_PERMISSION -> selectTrack()
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
        if (resultCode == RESULT_OK && (requestCode == REQUEST_CAMERA_PERMISSION)) {
            if (AlertBottomSheetFragment.getResult(data) == OK)
                startActivity(requireContext().appSettingsIntent())
        }
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            REQUEST_CAMERA -> {
                presenter.addToPhotosQueue(photoPath)
            }
            REQUEST_STORAGE_PERMISSION -> {
                selectTrack()
            }
            REQUEST_TRACK -> {
                val uri = data?.data as Uri
                filesHandler.saveTrackFileToTmpDirectory(uri) {
                    presenter.uploadTracks(it)
                }
            }
        }

    }

    override fun showSuccessUpload() {
        Snackbar.make(fabPhoto, getString(R.string.files_success_upload), Snackbar.LENGTH_LONG)
                .show()
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
        private const val REQUEST_STORAGE_PERMISSION = 2917
        private const val REQUEST_TRACK = 571
        private const val REQUEST_CAMERA_PERMISSION = 1917
        private const val TAG_CAMERA_PERMISSION = "phone_permission"
        fun newInstance(): FilesFragment = FilesFragment()
    }
}