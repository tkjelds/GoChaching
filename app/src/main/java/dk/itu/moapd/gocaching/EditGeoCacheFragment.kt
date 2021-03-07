package dk.itu.moapd.gocaching

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class EditGeoCacheFragment : Fragment() {
    private val geoCache = GeoCache("","", 0)
    private lateinit var cacheText : EditText
    private lateinit var whereText : EditText
    private lateinit var infoText : TextView
    private lateinit var updateButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_geo_cache,container,false)
        cacheText = view.findViewById(R.id.cache_text)
        whereText = view.findViewById(R.id.where_text)
        infoText = view.findViewById(R.id.info_text)
        updateButton = view.findViewById(R.id.fragment_button)
        updateButton.setText(R.string.update_button)
        return view
    }

    override fun onStart() {
        super.onStart()
        geoCacheDB = GeoCacheDB.get(activity as Context)
        updateButton.setOnClickListener { if (cacheText.text.isNotEmpty() && whereText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = whereText.text.toString().trim()

            geoCache.setCache(cache)
            geoCache.setWhere(where)
            geoCacheDB.updateGeoCache(cache, where)
            GoCachingFragment.adapter.notifyDataSetChanged()
            //AddGeoCacheActivity.adapter.notifyDataSetChanged()
            cacheText.text.clear()
            whereText.text.clear()

            updateUI()
        } }
    }

    companion object {
        lateinit var geoCacheDB : GeoCacheDB
        //lateinit var adapter: GeoCacheArrayAdapter
    }

    private fun updateUI() {
        infoText.setText(geoCache.toString())
    }
}