package com.apex.codeassesment.ui.location

import android.annotation.SuppressLint
import android.os.Looper
import com.apex.codeassesment.data.model.Coordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class LocationClient @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    private var isLocationServiceIsRunning = false
    private var mLocationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startSendingLocation(listener: Listener) {
        if (!isLocationServiceIsRunning) {
            isLocationServiceIsRunning = true
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    if (p0.locations.isNotEmpty()) {
                        val currentLocation = p0.locations.last()
                        listener.onNewLocation(
                            Coordinates(
                                latitude = currentLocation.latitude,
                                longitude = currentLocation.longitude
                            )
                        )
                    }
                }
            }

            val mLocationRequest = LocationRequest.create()
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 10000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback!!,
                Looper.getMainLooper()
            )

        }
    }

    fun onDestroy() {
        mLocationCallback?.let {
            isLocationServiceIsRunning = false
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }

    interface Listener {
        fun onNewLocation(coordinates: Coordinates)
    }
}