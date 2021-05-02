package dk.itu.moapd.gocaching.controller

import dk.itu.moapd.gocaching.GeoCache
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.adapter
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.model.database.Category
import dk.itu.moapd.gocaching.model.database.Difficulty
import java.io.File
import java.net.URI
import java.util.*

class AddGeoCacheFragment : Fragment() {

    private lateinit var cacheText : EditText
    private lateinit var infoText : TextView
    private lateinit var addButton : Button
    private lateinit var difSpin : Spinner
    private lateinit var catSpin : Spinner
    private var dif = Difficulty.EASY
    private var cat = Category.DEFAULT
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_geo_cache,container,false)
        cacheText = view.findViewById(R.id.cache_text)
        infoText = view.findViewById(R.id.info_text)
        addButton = view.findViewById(R.id.fragment_button)
        difSpin = view.findViewById(R.id.difficulty_spinner)
        catSpin = view.findViewById(R.id.category_spinner)
        ArrayAdapter.createFromResource(
                this.context,
                R.array.difficulty_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difSpin.adapter = adapter
        }
        ArrayAdapter.createFromResource(
                this.context,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            catSpin.adapter = adapter
        }
        return view
    }


    override fun onStart() {
        super.onStart()
        assert(arguments != null)
        var long = arguments!!.getDouble("longitude")
        var lat = arguments!!.getDouble("latitude")


        difSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                dif =  Difficulty.valueOf(p0!!.getItemAtPosition(p2).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                dif = Difficulty.EASY
            }

        }

        catSpin.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cat =  Category.valueOf(p0!!.getItemAtPosition(p2).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                cat = Category.DEFAULT
            }

        }

        val mGPSCollector = GPSTranslator(this.context!!)
        addButton.setOnClickListener { if (cacheText.text.isNotEmpty()){
            val cache = cacheText.text.toString().trim()
            val where = mGPSCollector.getAddress(long, lat)
            val gc = GeoCache(
                    where = where,
                    cache = cache,
                    long_ = long,
                    lat = lat,
                    category = cat,
                    difficulty = dif
            )
            geoCacheVM.insert(gc)
            cacheText.text.clear()
            adapter.notifyDataSetChanged()
            infoText.setText(where)
        } }
    }


}