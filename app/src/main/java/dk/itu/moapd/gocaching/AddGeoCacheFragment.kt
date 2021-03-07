package dk.itu.moapd.gocaching

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AddGeoCacheFragment : Fragment() {


        private val geoCache = GeoCache("","", 0)
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
        geoCacheDB = GeoCacheDB.get(activity as Context)
        //adapter = GeoCacheArrayAdapter(this, geoCacheDB.getGeoCaches())
        addButton.setOnClickListener { if (cacheText.text.isNotEmpty() && whereText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = whereText.text.toString().trim()
            geoCache.setCache(cache)
            geoCache.setWhere(where)
            geoCacheDB.addGeoCache(cache, where)
            GoCachingFragment.adapter.notifyDataSetChanged()
            cacheText.text.clear()
            whereText.text.clear()
            updateUI()
            System.out.println(geoCacheDB.getGeoCaches().size)
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