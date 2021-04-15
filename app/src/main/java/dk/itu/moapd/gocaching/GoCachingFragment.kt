package dk.itu.moapd.gocaching

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_go_caching.*
import kotlinx.android.synthetic.main.list_geo_cache.*
import java.sql.Date
import java.text.DateFormat
import java.util.*

class GoCachingFragment: Fragment() {
    companion object {
        lateinit var geoCacheDB : GeoCacheDB
        lateinit var adapter: GeoCacheArrayAdapter
        lateinit var radapter: GeoCacheRecyclerAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geoCacheDB = GeoCacheDB.get(activity as Context)
        val geoCaches = geoCacheDB.getGeoCaches()
        radapter = GeoCacheRecyclerAdapter(geoCaches)
        adapter = GeoCacheArrayAdapter(activity as Context, geoCaches)
        cache_recycler_view.layoutManager = LinearLayoutManager(context)
        cache_recycler_view.adapter = radapter
    }
    override fun onStart() {
        super.onStart()


        addCache.setOnClickListener {
            val intent = Intent(activity, AddGeoCacheActivity::class.java)
            startActivity(intent)
        }

        editCache.setOnClickListener {
            val intent = Intent(activity,EditGeoCacheActivity::class.java)
            startActivity(intent)
        }
        showList.setOnClickListener{
            radapter.notifyDataSetChanged()
        }
    }
    inner class GeoCacheViewHolder(view:View):
        RecyclerView.ViewHolder(view){
        val cache: TextView = view.findViewById(R.id.cache_text)
        val where: TextView = view.findViewById(R.id.where_text)
        val date: TextView = view.findViewById(R.id.date_text)
        val updateDate: TextView = view.findViewById(R.id.updatedate_text)

    }
    inner class GeoCacheRecyclerAdapter(var geoCaches:List<GeoCache>):
        RecyclerView.Adapter<GeoCacheViewHolder>() {
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
                geoCacheDB.deleteGeoCache(geoCaches[holder.absoluteAdapterPosition])
                radapter.notifyDataSetChanged()
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