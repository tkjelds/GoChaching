package dk.itu.moapd.gocaching

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_go_caching.*

class GoCachingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_caching)
        add_cache_button.setOnClickListener {
            val intent = Intent(this, AddGeoCacheActivity::class.java)
            startActivity(intent)
        }

        edit_cache_button.setOnClickListener {
            val intent = Intent(this,EditGeoCacheActivity::class.java)
            startActivity(intent)
        }

    }

}