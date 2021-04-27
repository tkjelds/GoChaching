package dk.itu.moapd.gocaching.controller

import MapsFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.moapd.gocaching.R


class MapsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        var fragment = supportFragmentManager
                .findFragmentById(R.id.fragment)

        if (fragment == null) {
            val longitude = intent.getDoubleExtra("longitude", 0.0)
            val latitude = intent.getDoubleExtra("latitude", 0.0)

            val bundle = Bundle().apply {
                putDouble("longitude", longitude)
                putDouble("latitude", latitude)
            }

            fragment = MapsFragment().apply {
                arguments = bundle
            }

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit()
        }
    }

}