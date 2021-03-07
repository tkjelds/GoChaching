package dk.itu.moapd.gocaching

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_add_geo_cache.*
import java.util.*

class AddGeoCacheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)
        val currentFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){
            val fragment = AddGeoCacheFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit()
        }
    }
}