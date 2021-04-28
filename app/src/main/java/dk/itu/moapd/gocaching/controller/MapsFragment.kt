import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.GeoCache

class MapsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        assert(arguments != null)
        val longitude = arguments!!.getDouble("longitude")
        val latitude = arguments!!.getDouble("latitude")
        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync { googleMap ->
            val latLng = LatLng(latitude, longitude)
            googleMap.apply {
                addMarker(MarkerOptions().position(latLng)
                        .title("Current Location"))
                geoCacheVM.getGeoCaches().observe(this@MapsFragment, Observer<List<GeoCache>> {
                    if (it != null) {
                        for (cache in it){
                            val pos = LatLng(cache.lat,cache.long_)
                            addMarker(MarkerOptions().position(pos).title(cache.cache))
                        }
                }})
                mapType = GoogleMap.MAP_TYPE_NORMAL
                animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
            }
        }

        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .commit()

        return view
    }

}