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

class AddGeoCacheFragment : Fragment() {


        private lateinit var cacheText : EditText
        private lateinit var whereText : EditText
        private lateinit var infoText : TextView
        private lateinit var addButton : Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_geo_cache,container,false)
        cacheText = view.findViewById(R.id.cache_text)
        whereText = view.findViewById(R.id.where_text)
        infoText = view.findViewById(R.id.info_text)
        addButton = view.findViewById(R.id.fragment_button)
        addButton.setText(R.string.add_button)
        return view
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener { if (cacheText.text.isNotEmpty() && whereText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = whereText.text.toString().trim()
            GoCachingFragment.geoCacheVM.insert(GeoCache(
                where = where,
                cache = cache
            ))
            cacheText.text.clear()
            whereText.text.clear()
            GoCachingFragment.adapter.notifyDataSetChanged()
            infoText.setText(R.string.cache_added)
        } }
    }
}