package dk.itu.moapd.gocaching.controller

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.adapter
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.geoCacheVM
import kotlinx.android.synthetic.main.fragment_go_caching.*

class GoCachingFragmentDialog(geocache:GeoCache,pos:Int,lon:Double,lat:Double): DialogFragment() {
    val geocache = geocache
    val pos = pos
    val lat = lat
    val lon = lon
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.geocache_menu)
                    .setPositiveButton(R.string.delete_geocache,
                            DialogInterface.OnClickListener { dialog, id ->

                        })
                    .setNegativeButton(R.string.edit_cache_button,
                            DialogInterface.OnClickListener { dialog, id ->
                                val intent = Intent(activity, EditGeoCacheActivity::class.java).apply {
                                    putExtra("longitude", lon)
                                    putExtra("latitude", lat)
                                   // putExtra("cache",geocache.cache)
                                }
                                startActivity(intent)
                            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        adapter.notifyDataSetChanged()
        super.onDestroyView()
    }
}