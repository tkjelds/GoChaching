package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.moapd.gocaching.R

class ProfileActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)
        var currentFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null){
            val email = intent.getStringExtra("email")

            val bundle = Bundle().apply {
                putString("email", email)
            }

            currentFragment = ProfileFragment().apply {
                arguments = bundle
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container,currentFragment)
                    .commit()
        }
    }
}