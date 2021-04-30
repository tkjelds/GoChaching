package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import kotlinx.android.synthetic.main.admin_list_caches.*

class AdminFragment:Fragment() {
    private lateinit var nameTextField: TextView
    private lateinit var emailTextField: TextView
    private lateinit var adminAdapter: AdminRecyclerAdapter
    private lateinit var userEmail:String
    private lateinit var user: User
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.admin_fragment,container,false)
        nameTextField = view.findViewById(R.id.admin_profile_name)
        emailTextField = view.findViewById(R.id.admin_profile_email)
        adminAdapter = AdminRecyclerAdapter()
        geoCacheVM.getGeoCaches().observe(this, Observer<List<GeoCache>>{
            var caches = it.filter { cache -> !cache.isApproved }
            adminAdapter.setAdminCaches(caches)
        })
        userEmail = arguments!!.getString("email")
        geoCacheVM.getUsers().observe(this, Observer<List<User>> {
            user = it.find{ user -> user.email == userEmail}!!
        })
        return view
    }

    override fun onStart() {
        super.onStart()
        nameTextField.text = user.name.toString()
        emailTextField.text = user.email.toString()

    }
    inner class AdminViewHolder(view:View)
        :RecyclerView.ViewHolder(view){
        val lat:TextView = view.findViewById(R.id.admin_lat)
        val long:TextView = view.findViewById(R.id.admin_long)
        val difficulty:TextView = view.findViewById(R.id.admin_difficulty)
        val category:TextView = view.findViewById(R.id.admin_category)
        val approveButton:Button = view.findViewById(R.id.admin_approve_button)
        val deleteButton:Button = view.findViewById(R.id.admin_delete_button)

    }

    inner class AdminRecyclerAdapter():
            RecyclerView.Adapter<AdminViewHolder>() {
        private var geoCaches :ArrayList<GeoCache> = ArrayList()
        fun setAdminCaches(geoCaches:List<GeoCache>){

        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): AdminViewHolder {
            val layout = layoutInflater.inflate(R.layout.admin_list_caches,parent,false)
            return AdminViewHolder(layout)
        }

        override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
            val geoCache = geoCaches[position]
            holder.apply {
                lat.text = geoCache.lat.toString()
                long.text = geoCache.long_.toString()
                difficulty.text = geoCache.difficulty.toString()
                category.text = geoCache.category.toString()
            }

            holder.approveButton.setOnClickListener {
                val gc = geoCaches[holder.absoluteAdapterPosition]
                geoCacheVM.delete(gc)
                this.notifyDataSetChanged()
                geoCacheVM.insert(GeoCache(
                        gcid =  gc.gcid,
                        where = gc.where,
                        date = gc.date,
                        isApproved = true,
                        difficulty = gc.difficulty,
                        cache = gc.cache,
                        lat = gc.lat,
                        long_ = gc.long_,
                        category = gc.category
                ))
                this.notifyDataSetChanged()
            }

            holder.deleteButton.setOnClickListener {
                val pos = holder.absoluteAdapterPosition
                val gc = geoCaches[pos]
                geoCacheVM.delete(gc)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos,itemCount)
            }

        }

        override fun getItemCount(): Int {
            return geoCaches.size
        }

    }
}

