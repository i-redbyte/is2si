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
import kotlinx.android.synthetic.main.layout_test_point.*
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
import ru.is2si.sisi.presentation.model.LocationView
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
        if (point.pointNameStr == TEST_POINT) {
            etTestLatitude.setText("0.0")
            etTestLongitude.setText("0.0")
            testContainer.show()
        }
        fabPhoto.onClick { checkPhotoPermission() }
        btnFixCenter.onClick { presenter.getLocation() }
        cbAccuracy.setOnCheckedChangeListener { _, b -> presenter.isAccuracy = b }
    }

    override fun showTestCoordinates(location: LocationView) {
        if (point.pointNameStr == TEST_POINT) point.location = location
        with(location) {
            etTestLatitude.setText(latitude.toString())
            etTestLongitude.setText(longitude.toString())
            etTestRadius.setText(point.minRadius.toString())
        }
    }

    private fun getHitTextAndMeters(location: LocationView): Pair<String, Float> {
        val meters = point.location.metersDistanceTo(location)
        return if (meters <= point.minRadius)
            getString(R.string.point_hit) to meters
        else
            getString(R.string.point_not_hit) to meters
    }

    override fun showTestAccuracyCoordinates(location: LocationView, counter: Int) {
        val latitude = location.latitude
        val longitude = location.longitude
        val textHit = getHitTextAndMeters(location).first
        val meters = getHitTextAndMeters(location).second
        val text = tvAccuracy.text.toString()
        val finishText ="$text$counter) Широта: $latitude Долгота: $longitude $textHit ${String.format("%.2f", meters)} м.\n"
        tvAccuracy.text = finishText
    }

    override fun showPhotoData(location: LocationView) {
        showMain()
        with(location) {
            tvLatitude.text = getString(R.string.point_latitude_value, latitude)
            tvLongitude.text = getString(R.string.point_longitude_value, longitude)
        }
        if (point.pointNameStr == TEST_POINT) {
            point.location.longitude = etTestLongitude.text.toString().toDouble()
            point.location.latitude = etTestLatitude.text.toString().toDouble()
            point.minRadius = etTestRadius.text.toString().toDouble()
        }
        val meters = getHitTextAndMeters(location).second
        tvHit.text = getHitTextAndMeters(location).first
        tvDistanceToCenter.text =
                getString(R.string.point_distance_to_center_value, String.format("%.2f", meters))
    }

    private fun checkPhotoPermission() {
        when (beforeRequestPermissions(
                REQUEST_CAMERA,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )) {
            BeforeRequestPermissionResult.AlreadyGranted -> presenter.onCameraClick(point.pointNameStr == TEST_POINT)
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_CAMERA,
                        true,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            BeforeRequestPermissionResult.Requested -> Unit
        }
    }

    override fun checkPermission() {
        when (beforeRequestPermissions(
                REQUEST_POINT_PERMISSION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )) {
            BeforeRequestPermissionResult.AlreadyGranted -> presenter.permissionOk()
            BeforeRequestPermissionResult.ShowRationale -> {
                beforeRequestPermissions(
                        REQUEST_POINT_PERMISSION,
                        true,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
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
                    REQUEST_CAMERA -> presenter.onCameraClick(point.pointNameStr == TEST_POINT)
                    REQUEST_POINT_PERMISSION -> presenter.permissionOk()
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
        private const val REQUEST_POINT_PERMISSION = 1918
        private const val TAG_CAMERA_PERMISSION = "phone_permission"
        const val TEST_POINT = "0/0"

        @JvmStatic
        fun forPoint(point: PointView) = PointFragment().withArguments {
            putParcelable(ARG_POINT, point)
        }
    }
}