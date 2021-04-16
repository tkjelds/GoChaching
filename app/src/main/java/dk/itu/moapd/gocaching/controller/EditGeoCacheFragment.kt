package dk.itu.moapd.gocaching.controller

import dk.itu.moapd.gocaching.GeoCache
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dk.itu.moapd.gocaching.*
import dk.itu.moapd.gocaching.model.database.*
import java.util.*

class EditGeoCacheFragment : Fragment() {
    private lateinit var geoCache : GeoCache
    private lateinit var cacheText : EditText
    private lateinit var whereText : EditText
    private lateinit var infoText : TextView
    private lateinit var updateButton : Button
    //private lateinit var geoCacheVM : GeoCacheViewModel
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
        updateButton.setOnClickListener { if (cacheText.text.isNotEmpty() && whereText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = whereText.text.toString().trim()
            geoCache.cache = cache
            geoCache.where = where
            geoCache.updateDate = Date()
            GoCachingFragment.geoCacheVM.update(geoCache)
            GoCachingFragment.adapter.notifyDataSetChanged()
            cacheText.text.clear()
            whereText.text.clear()
            updateUI()
        } }
    }

    private fun updateUI() {
        infoText.setText(geoCache.toString())
    }
}