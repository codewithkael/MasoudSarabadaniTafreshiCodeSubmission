package com.apex.codeassesment.ui.location

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.databinding.ActivityLocationBinding
import com.apex.codeassesment.di.MainComponent
import com.permissionx.guolindev.PermissionX
import javax.inject.Inject


// TODO (Optional Bonus 8 points): Calculate distance between 2 coordinates using phone's location
class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding

    @Inject
    lateinit var locationClient: LocationClient
    private val distanceHelper = DistanceHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        (applicationContext as MainComponent.Injector).mainComponent.inject(this)
        val latitudeRandomUser = intent.getDoubleExtra("user-latitude-key", 0.0)
        val longitudeRandomUser = intent.getDoubleExtra("user-longitude-key", 0.0)


        binding.locationRandomUser.text = getString(
            R.string.location_random_user, latitudeRandomUser.toString(),
            longitudeRandomUser.toString()
        )
        binding.locationCalculateButton.setOnClickListener {
            calculateLocationDifference(latitudeRandomUser, longitudeRandomUser)
        }
    }

    private fun calculateLocationDifference(targetLat: Double, targetLon: Double) {
        PermissionX.init(this)
            .permissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .request { allGranted, _, _ ->
                if (allGranted) {
                    locationClient.startSendingLocation(object : LocationClient.Listener {
                        override fun onNewLocation(coordinates: Coordinates) {
                            if (coordinates.latitude != null && coordinates.longitude != null) {
                                val difference = distanceHelper.distance(
                                    targetLat,
                                    targetLon,
                                    coordinates.latitude,
                                    coordinates.longitude,
                                    'M'
                                )
                                binding.locationPhone.text = getString(
                                    R.string.location_phone,
                                    coordinates.latitude.toString(),
                                    coordinates.longitude.toString()
                                )
                                binding.locationDistance.text =
                                    getString(R.string.location_result_miles, difference)
                            }
                        }
                    })
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.onDestroy()
    }
}
