package dk.itu.moapd.gocaching.controller

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.*

class GPSDataCollector(context: Context) {


    private var context = context


    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
        private const val UPDATE_INTERVAL = 5000L
        private const val FASTEST_INTERVAL = 5000L
    }

    private fun checkPermission() =
            ActivityCompat.checkSelfPermission(
                    this.context!!, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            context!!, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED

    public fun startLocationUpdates() {
        if (checkPermission())

            return
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest, mLocationCallback, null
        )
    }

    public fun stopLocationUpdates() {
        mFusedLocationProviderClient
                .removeLocationUpdates(mLocationCallback)
    }

    fun getCurrentCoords(): Pair<Double, Double> {
        var mLongitude = 0.0
        var mLatitude = 0.0
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context!!)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    mLongitude = location.longitude
                    mLatitude = location.latitude
                }
            }
        }
        startLocationUpdates()
        Thread.sleep(500)
        stopLocationUpdates()
        return Pair(mLongitude, mLatitude)
    }

    public fun getAddress(longitude: Double, latitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val stringBuilder = StringBuilder()

        try {
            val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                stringBuilder.apply {
                    append(address.getAddressLine(0)).append("\n")
                    append(address.locality).append("\n")
                    append(address.postalCode).append("\n")
                    append(address.countryName)
                }
            } else
                return "No address found"

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return stringBuilder.toString()
    }
}