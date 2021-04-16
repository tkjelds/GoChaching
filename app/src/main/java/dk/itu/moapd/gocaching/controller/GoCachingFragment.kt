package dk.itu.moapd.gocaching.controller

import dk.itu.moapd.gocaching.GeoCache
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.model.database.*
import kotlinx.android.synthetic.main.fragment_go_caching.*
import java.text.DateFormat

class GoCachingFragment: Fragment() {

    private lateinit var addCache: Button
    private lateinit var editCache: Button
    private lateinit var showList: Button

    companion object{
        lateinit var adapter: GeoCacheRecyclerAdapter
        lateinit var geoCacheVM: GeoCacheViewModel
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_go_caching,container,false)
        addCache = view.findViewById(R.id.add_cache_button) as Button
        editCache = view.findViewById(R.id.edit_cache_button) as Button
        showList = view.findViewById(R.id.list_caches_button) as Button
        adapter = GeoCacheRecyclerAdapter()
        geoCacheVM = ViewModelProviders.of(this).get(GeoCacheViewModel::class.java)
        geoCacheVM.getGeoCaches().observe(this, Observer<List<GeoCache>> {
            adapter.setGeoCaches(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cache_recycler_view.layoutManager = LinearLayoutManager(activity)
        cache_recycler_view.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()

        addCache.setOnClickListener {
            val intent = Intent(activity, AddGeoCacheActivity::class.java)
            startActivity(intent)
        }

        editCache.setOnClickListener {
            val intent = Intent(activity, EditGeoCacheActivity::class.java)
            startActivity(intent)
        }
        showList.setOnClickListener{
            adapter.notifyDataSetChanged()
        }
    }
    inner class GeoCacheViewHolder(view:View):
        RecyclerView.ViewHolder(view){
        val cache: TextView = view.findViewById(R.id.cache_text)
        val where: TextView = view.findViewById(R.id.where_text)
        val date: TextView = view.findViewById(R.id.date_text)
        val updateDate: TextView = view.findViewById(R.id.updatedate_text)

    }

    inner class GeoCacheRecyclerAdapter():
        RecyclerView.Adapter<GeoCacheViewHolder>() {

        private var geoCaches = emptyList<GeoCache>()

        fun setGeoCaches(geoCaches_:List<GeoCache>){
            geoCaches = geoCaches_
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): GeoCacheViewHolder {
            val layout = layoutInflater.inflate(R.layout.list_geo_cache,parent,false)
            return GeoCacheViewHolder(layout)
        }

        override fun onBindViewHolder(holder: GeoCacheViewHolder,
                                      position: Int) {
            val geoCache = geoCaches[position]
            holder.apply {
                cache.text = geoCache.cache
                where.text = geoCache.where
                date.text = formatDate(geoCache.date)
                updateDate.text = formatDate(geoCache.updateDate)
            }
            holder.cache.setOnLongClickListener() {
                geoCacheVM.delete(geoCaches[holder.absoluteAdapterPosition])
                adapter.notifyDataSetChanged()
                true
            }

        }

        override fun getItemCount(): Int {
            return geoCaches.size
        }

    }
    fun formatDate(date: java.util.Date) : String{
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
    }

}