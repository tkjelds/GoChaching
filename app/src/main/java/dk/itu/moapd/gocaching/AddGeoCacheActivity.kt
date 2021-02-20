package dk.itu.moapd.gocaching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_geo_cache.*
import java.util.*

class AddGeoCacheActivity : AppCompatActivity() {
    private val geoCache = GeoCache("","", Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_geo_cache)
        add_button.setOnClickListener { if (cache_text.text.isNotEmpty() && where_text.text.isNotEmpty()){
            val cache = cache_text.text.toString().trim()
            val where = where_text.text.toString().trim()

            geoCache.setCache(cache)
            geoCache.setWhere(where)

            cache_text.text.clear()
            where_text.text.clear()

            updateUI()
        } }
    }

    private fun updateUI() {
        info_text.setText(geoCache.toString())
    }
}