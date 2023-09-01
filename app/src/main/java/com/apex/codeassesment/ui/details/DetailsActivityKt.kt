package com.apex.codeassesment.ui.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityDetailsBinding
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.ui.location.LocationActivity
import com.bumptech.glide.RequestManager
import javax.inject.Inject

@Suppress("DEPRECATION")
class DetailsActivityKt : AppCompatActivity() {

    private lateinit var views: ActivityDetailsBinding
    private var user: User? = null

    @Inject
    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(views.root)
        init()
    }

    private fun init() {
        (applicationContext as MainComponent.Injector).mainComponent.inject(this)
        user = intent.getParcelableExtra("saved-user-key")
        user?.let { noneNullUser ->
            views.apply {
                glide.load(noneNullUser.picture?.large).into(detailsImage)
                detailsName.text = noneNullUser.email
                detailsEmail.text = getString(R.string.details_email, noneNullUser.email)
                detailsAge.text = noneNullUser.dob?.age.toString()
                val coordinates = noneNullUser.location?.coordinates
                detailsLocation.text = getString(
                    R.string.details_location,
                    coordinates?.latitude.toString(),
                    coordinates?.longitude.toString()
                )
                coordinates?.let { noneNullCoordinates ->
                    detailsLocationButton.setOnClickListener { navigateLocation(noneNullCoordinates) }
                }
            }
        } ?: kotlin.run {
            Toast.makeText(this, getString(R.string.user_from_intent_is_null), Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun navigateLocation(coordinates: Coordinates) {
        Log.d("TAG", "navigateLocation: $coordinates")
        startActivity(Intent(this, LocationActivity::class.java).apply {
            putExtra(
                "user-latitude-key", coordinates.latitude
            )
            putExtra("user-longitude-key", coordinates.longitude)
        })
    }
}