package dk.itu.moapd.gocaching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class GeoCacheArrayAdapter(context: Context, geoCaches:List<GeoCache>) :
        ArrayAdapter<GeoCache>(context,R.layout.list_geo_cache,geoCaches) {
    private class GeoCacheViewHolder {
        var cache: TextView? = null
        var where: TextView? = null
        var date: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: GeoCacheViewHolder
        if (view == null)  {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_geo_cache, parent, false)
            viewHolder = GeoCacheViewHolder()
            viewHolder.apply { this.cache = view.findViewById<View>(R.id.cache_text) as TextView
                               this.where = view.findViewById<View>(R.id.where_text) as TextView
                               this.date  = view.findViewById<View>(R.id.date_text) as TextView
                            }
                            }
        else
            viewHolder = view.tag as GeoCacheViewHolder
        val geoCache = getItem(position)
        viewHolder.apply {  this.cache!!.text = geoCache?.cache
                            this.where!!.text = geoCache?.where
                            this.date!!.text = geoCache?.date.toString()
                        }
        view?.tag = viewHolder
        return view!!
    }
}