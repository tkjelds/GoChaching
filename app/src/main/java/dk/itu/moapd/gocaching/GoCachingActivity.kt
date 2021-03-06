package dk.itu.moapd.gocaching

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_go_caching.*
import java.util.*

class GoCachingActivity : AppCompatActivity() {
    companion object {
        lateinit var geoCacheDB : GeoCacheDB
        lateinit var adapter: GeoCacheArrayAdapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)
        geoCacheDB = GeoCacheDB.get(this)
        val geoCaches = geoCacheDB.getGeoCaches()
        adapter = GeoCacheArrayAdapter(this, geoCaches)
        //cache_list_view.adapter = adapter
        add_cache_button.setOnClickListener {
            val intent = Intent(this, AddGeoCacheActivity::class.java)
            startActivity(intent)
        }

        edit_cache_button.setOnClickListener {
            val intent = Intent(this,EditGeoCacheActivity::class.java)
            startActivity(intent)
        }

        list_caches_button.setOnClickListener {         cache_list_view.adapter = adapter }

    }

}