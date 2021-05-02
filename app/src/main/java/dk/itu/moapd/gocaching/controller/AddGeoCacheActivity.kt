package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.moapd.gocaching.R

class AddGeoCacheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)
        var currentFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val longitude = intent.getDoubleExtra("longitude", 0.0)
            val latitude = intent.getDoubleExtra("latitude", 0.0)

            val bundle = Bundle().apply {
                putDouble("longitude", longitude)
                putDouble("latitude", latitude)
            }

            currentFragment = AddGeoCacheFragment().apply {
                arguments = bundle
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit()
        }
    }
}