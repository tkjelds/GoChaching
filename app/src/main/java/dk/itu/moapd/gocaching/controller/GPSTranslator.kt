package dk.itu.moapd.gocaching.controller

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

class GPSTranslator(context: Context) {
    private var context = context

    fun getAddress(longitude: Double, latitude: Double): String {
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