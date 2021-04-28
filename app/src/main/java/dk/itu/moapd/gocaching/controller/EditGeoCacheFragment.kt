package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import dk.itu.moapd.gocaching.*
import kotlinx.android.synthetic.main.fragment_geo_cache.*
import java.util.*

class EditGeoCacheFragment : Fragment() {
    private lateinit var cacheText : EditText
    private lateinit var infoText : TextView
    private lateinit var updateButton : Button
    private var long = 0.toDouble()
    private var lat = 0.toDouble()
    private var cache = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_geo_cache,container,false)
        cacheText = view.findViewById(R.id.cache_text)
        //whereText = view.findViewById(R.id.where_text)
        infoText = view.findViewById(R.id.info_text)
        updateButton = view.findViewById(R.id.fragment_button)
        updateButton.setText(R.string.update_button)
        assert(arguments != null)
        long = arguments!!.getDouble("longitude")
        lat = arguments!!.getDouble("latitude")
        cache = arguments!!.getString("cache")
        return view
    }

    override fun onStart() {
        super.onStart()
        cacheText.setText(cache)
        val mGPSCollector = GPSTranslator(this.context!!)
        updateButton.setOnClickListener { if (cacheText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            var where = mGPSCollector.getAddress(long,lat)
            //GoCachingFragment.geoCacheVM.update(cache,where,Date())
            GoCachingFragment.adapter.notifyDataSetChanged()
            cacheText.text.clear()
            infoText.setText(where)

        } }
    }
}