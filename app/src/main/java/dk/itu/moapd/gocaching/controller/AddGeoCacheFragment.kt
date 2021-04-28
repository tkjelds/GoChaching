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
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.geoCacheVM

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
        assert(arguments != null)
        var long = arguments!!.getDouble("longitude")
        var lat = arguments!!.getDouble("latitude")
        val mGPSCollector = GPSTranslator(this.context!!)
        addButton.setOnClickListener { if (cacheText.text.isNotEmpty() && whereText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = mGPSCollector.getAddress(long, lat)
            val gc = GeoCache(
                    where = where,
                    cache = cache,
                    long_ = long,
                    lat = lat)
            geoCacheVM.insert(gc)
            cacheText.text.clear()
            whereText.text.clear()
            GoCachingFragment.adapter.notifyDataSetChanged()
            infoText.setText(where)
        } }
    }


}