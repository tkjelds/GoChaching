package dk.itu.moapd.gocaching

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_go_caching.*

class GoCachingActivity : AppCompatActivity() {

    private val geoCache = GeoCache("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)

        search_button.setOnClickListener { if (cache_text.text.isNotEmpty() && where_text.text.isNotEmpty()){
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
        searched_text.setText(geoCache.toString())
    }
}