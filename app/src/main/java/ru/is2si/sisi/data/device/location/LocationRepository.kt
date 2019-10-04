package ru.is2si.sisi.data.device.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import io.reactivex.Observable
import io.reactivex.Single
import ru.is2si.sisi.BuildConfig
import ru.is2si.sisi.R
import ru.is2si.sisi.base.device.location.*
import ru.is2si.sisi.base.network.NetworkSettings
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationRepository @Inject constructor(
        private val context: Context,
        private val networkSettings: NetworkSettings
) : LocationDataSource {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

    private var updateLocationThreadRef: WeakReference<HandlerThread>? = null

    override fun getLocation(): Single<Location> = observeLocationChanges().firstOrError()

    override fun subscribeUpdateLocation(): Observable<Location> = observeLocationChanges()

    private fun observeLocationChanges(): Observable<Location> {
        return Observable.create { emitter ->
            if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
            ) {
                emitter.onError(LocationPermissionDeniedException())
                return@create
            }

            var result: Either<Location>? = null
            val resultLock = CountDownLatch(1)
            val timeoutHandler = Handler(getUpdateLocationLooper())
            val locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setFastestInterval(0)
                    .setInterval(0)

            val errorFunction = fun(error: Throwable) {
                timeoutHandler.removeCallbacksAndMessages(null)
                result = Either.left(error)
                resultLock.countDown()
            }
            val requestLocation = {
                val locationCallback = WeakLocationCallback(object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        timeoutHandler.removeCallbacksAndMessages(null)
                        val location = locationResult.lastLocation
                        result = Either.right(Location(location.latitude, location.longitude))
                        resultLock.countDown()
                        if (BuildConfig.DEBUG)
                            Log.d("_debug", "Location: ${location.latitude}, ${location.longitude}")
                    }
                })

                timeoutHandler.postDelayed(
                        {
                            val message = context.getString(R.string.device_detect_location_timeout)
                            errorFunction(TimeoutDetectLocationException(message))
                        },
                        TimeUnit.SECONDS.toMillis(networkSettings.detectLocationTimeout)
                )
                fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        getUpdateLocationLooper()
                )
                emitter.setCancellable {
                    timeoutHandler.removeCallbacksAndMessages(null)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
            }

            val checkLocationSettings = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .build()
            LocationServices.getSettingsClient(context)
                    .checkLocationSettings(checkLocationSettings)
                    .addOnCompleteListener { response ->
                        tryToResolveLocationException(response, requestLocation, errorFunction)
                    }
            runCatching { resultLock.await() }
            result?.error?.also {
                if (emitter.isDisposed.not())
                    emitter.tryOnError(it)
            }
            result?.result?.also {
                if (emitter.isDisposed.not())
                    emitter.onNext(it)
            }
        }
    }

    private inline fun tryToResolveLocationException(
            response: Task<LocationSettingsResponse>,
            crossinline requestLocation: () -> Unit,
            crossinline requestError: (Exception) -> Unit
    ) {
        try {
            response.getResult(ApiException::class.java)
            requestLocation.invoke()
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvable = exception as ResolvableApiException
                        requestError(ResolvableLocationSettingsException(resolvable))
                    } catch (e: ClassCastException) {
                        // Ignore, should be an impossible error.
                        requestError(IncorrectLocationSettingsException())
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    requestError(IncorrectLocationSettingsException())
                }
            }
            if (BuildConfig.DEBUG)
                Log.e("_debug", "Location error:", exception)
        }
    }

    private fun getUpdateLocationLooper(): Looper {
        var updateLocationThread = updateLocationThreadRef?.get()
        if (updateLocationThread == null) {
            updateLocationThread = HandlerThread("update_location_thread").also { it.start() }
            updateLocationThreadRef = WeakReference(updateLocationThread)
        }
        return updateLocationThread.looper
    }

}

private class Either<T> private constructor(val error: Throwable?, val result: T?) {
    companion object {
        @JvmStatic
        fun <T> left(error: Throwable) = Either<T>(error, null)

        @JvmStatic
        fun <T> right(result: T) = Either(null, result)
    }
}