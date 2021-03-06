package dk.itu.moapd.gocaching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_geo_cache.*
import java.util.*


class EditGeoCacheActivity : AppCompatActivity() {
    private val geoCache = GeoCache("","",0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_geo_cache)
        geoCacheDB = GeoCacheDB.get(this)
        update_button.setOnClickListener { if (cache_text.text.isNotEmpty() && where_text.text.isNotEmpty()){
            val cache = cache_text.text.toString().trim()
            val where = where_text.text.toString().trim()

            geoCache.setCache(cache)
            geoCache.setWhere(where)
            geoCacheDB.updateGeoCache(cache, where)
            GoCachingActivity.adapter.notifyDataSetChanged()
            //AddGeoCacheActivity.adapter.notifyDataSetChanged()
            cache_text.text.clear()
            where_text.text.clear()

            updateUI()
        } }
    }
    companion object {
        lateinit var geoCacheDB : GeoCacheDB
        //lateinit var adapter: GeoCacheArrayAdapter
    }
    private fun updateUI() {
        info_text.setText(geoCache.toString())
    }
}