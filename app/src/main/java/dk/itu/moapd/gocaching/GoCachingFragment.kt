package dk.itu.moapd.gocaching

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_go_caching.*
import java.util.*

class GoCachingFragment: Fragment() {
    companion object {
        lateinit var geoCacheDB : GeoCacheDB
        lateinit var adapter: GeoCacheArrayAdapter
    }

    private lateinit var addCache: Button
    private lateinit var editCache: Button
    private lateinit var showList: Button

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_go_caching,container,false)
        addCache = view.findViewById(R.id.add_cache_button) as Button
        editCache = view.findViewById(R.id.edit_cache_button) as Button
        showList = view.findViewById(R.id.list_caches_button) as Button

        return view
    }


    override fun onStart() {
        super.onStart()
        geoCacheDB = GeoCacheDB.get(activity as Context)
        val geoCaches = geoCacheDB.getGeoCaches()
        adapter = GeoCacheArrayAdapter(activity as Context, geoCaches)
        addCache.setOnClickListener {
            val intent = Intent(activity, AddGeoCacheActivity::class.java)
            startActivity(intent)
        }

        editCache.setOnClickListener {
            val intent = Intent(activity,EditGeoCacheActivity::class.java)
            startActivity(intent)
        }

        showList.setOnClickListener {         cache_list_view.adapter = adapter }

        cache_list_view.setOnItemClickListener { parent, view, position, id ->
            adapter.getItem(position)?.let { geoCacheDB.deleteGeoCache(it) }
            adapter.notifyDataSetChanged()
        }
    }

}