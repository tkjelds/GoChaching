package dk.itu.moapd.gocaching.controller

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.view.NPALinearLayoutManager
import kotlinx.android.synthetic.main.fragment_go_caching.*
import java.text.DateFormat

class GoCachingFragment : Fragment() {

    private lateinit var addCache: Button
    private lateinit var registerCacheButton: Button
    private lateinit var showList: Button
    private lateinit var profileButton: Button
    private lateinit var refreshButton: Button
    private var mLongitude = 0.0
    private var mLatitude = 0.0
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private val permissions: ArrayList<String> = ArrayList()
    private var userEmail = ""
    private lateinit var user: User

    companion object {
        lateinit var adapter: GeoCacheRecyclerAdapter
        private const val ALL_PERMISSIONS_RESULT = 1011
        const val UPDATE_INTERVAL = 5000L
        const val FASTEST_INTERVAL = 5000L
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_go_caching, container, false)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.GET_ACCOUNTS)
        permissions.add(Manifest.permission.INTERNET)
        permissions.add(Manifest.permission.CAMERA)
        addCache = view.findViewById(R.id.add_cache_button) as Button
        profileButton = view.findViewById(R.id.profile_button) as Button
        registerCacheButton = view.findViewById(R.id.regsiter_cache_button) as Button
        showList = view.findViewById(R.id.list_caches_button) as Button
        refreshButton = view.findViewById(R.id.go_caching_refresh_button) as Button
        userEmail = arguments!!.getString("email")
        geoCacheVM.getUsers().observe(this, Observer<List<User>> {
            user = it.find { user -> user.email == userEmail }!!
        })
        adapter = GeoCacheRecyclerAdapter()
        geoCacheVM.getGeoCaches().observe(this, Observer<List<GeoCache>> {
            adapter.setGeoCaches(it.filter { cache -> cache.isApproved })
        })
        val permissionsToRequest = permissionsToRequest(permissions)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (permissionsToRequest.size > 0)
                requestPermissions(
                        permissionsToRequest.toTypedArray(),
                        ALL_PERMISSIONS_RESULT
                )
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
        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        adapter.notifyDataSetChanged()
        stopLocationUpdates()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cache_recycler_view.layoutManager = NPALinearLayoutManager(activity)
        cache_recycler_view.adapter = adapter
        adapter.notifyDataSetChanged()

        addCache.setOnClickListener {
            val intent = Intent(activity, AddGeoCacheActivity::class.java).apply {
                putExtra("longitude", mLongitude)
                putExtra("latitude", mLatitude)
            }
            startActivity(intent)
        }

        registerCacheButton.setOnClickListener {
            val intent = Intent(activity, RegisterGeoCacheActivity::class.java).putExtra("email", userEmail)
            startActivity(intent)
        }

        profileButton.setOnClickListener {

            var intent = Intent(activity, ProfileActivity::class.java).apply {
                putExtra("email", userEmail)
            }
            if (user.isAdmin) {
                intent = Intent(activity, AdminActivity::class.java).apply {
                    putExtra("email", userEmail)
                }
            }
            startActivity(intent)
        }

        showList.setOnClickListener {

            val intent = Intent(activity, MapsActivity::class.java).apply {
                putExtra("longitude", mLongitude)
                putExtra("latitude", mLatitude)
            }
            startActivity(intent)
        }

        refreshButton.setOnClickListener {
            adapter.notifyDataSetChanged()
        }
    }

    inner class GeoCacheViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {
        val cache: TextView = view.findViewById(R.id.cache_text)
        val where: TextView = view.findViewById(R.id.where_text)
        val date: TextView = view.findViewById(R.id.date_text)
        val updateDate: TextView = view.findViewById(R.id.updatedate_text)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        val photoView: ImageView = view.findViewById(R.id.cache_photo_view_list)
        val catView: TextView = view.findViewById(R.id.category_text)
        val difView: TextView = view.findViewById(R.id.difficulty_text)
        val captureButton: Button = view.findViewById(R.id.go_caching_capture_button)
    }

    inner class GeoCacheRecyclerAdapter :
            RecyclerView.Adapter<GeoCacheViewHolder>() {
        private var geoCaches: ArrayList<GeoCache> = ArrayList()

        fun setGeoCaches(geoCaches_: List<GeoCache>) {
            geoCaches = ArrayList(geoCaches_)
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): GeoCacheViewHolder {
            val layout = layoutInflater.inflate(R.layout.list_geo_cache, parent, false)
            return GeoCacheViewHolder(layout)
        }

        override fun onBindViewHolder(holder: GeoCacheViewHolder,
                                      position: Int) {
            val geoCache = geoCaches[position]
            holder.apply {
                cache.text = geoCache.cache
                where.text = geoCache.where
                date.text = formatDate(geoCache.date)
                updateDate.text = formatDate(geoCache.updateDate)
                catView.text = geoCache.category.toString()
                difView.text = geoCache.difficulty.toString()
                updatePhotoView(photoView, geoCache)
            }
            holder.deleteButton.setOnLongClickListener {
                val pos = holder.absoluteAdapterPosition
                geoCacheVM.delete(geoCaches[holder.absoluteAdapterPosition])
                geoCaches.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, itemCount)
                true
            }
            holder.captureButton.setOnClickListener {
                GoCachingFragmentDialog(geoCaches[holder.absoluteAdapterPosition],
                        position,
                        mLongitude,
                        mLatitude).show(childFragmentManager,
                        "GoCachingFragmentDialog")
            }

        }

        override fun getItemCount(): Int {
            return geoCaches.size
        }

        private fun updatePhotoView(photoView: ImageView, geoCache: GeoCache) {
            val photofile = geoCacheVM.getPhotoFile(geoCache)
            if (photofile.exists()) {
                val bitmap = PictureUtils().getScaledBitmap(photofile.path, requireActivity())
                photoView.setImageBitmap(bitmap)
            } else {
                photoView.setImageDrawable(null)
            }
        }

    }

    fun formatDate(date: java.util.Date): String {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
    }

    private fun permissionsToRequest(
            permissions: ArrayList<String>): ArrayList<String> {

        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (!hasPermission(permission))
                result.add(permission)
        return result
    }

    private fun hasPermission(permission: String) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                activity?.checkSelfPermission(permission) ==
                        PackageManager.PERMISSION_GRANTED
            else
                true

    private fun checkPermission() =
            ActivityCompat.checkSelfPermission(
                    context!!, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            context!!, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED

    //@SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
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

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient
                .removeLocationUpdates(mLocationCallback)
    }
}