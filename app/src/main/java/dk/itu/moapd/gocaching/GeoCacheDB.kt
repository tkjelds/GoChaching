package dk.itu.moapd.gocaching

import android.content.Context
import android.location.Geocoder
import java.util.*
import kotlin.collections.ArrayList

class GeoCacheDB private constructor(context:Context){
    private val geoCaches = ArrayList < GeoCache >()
    private val lastCache = GeoCache("","", randomDate())

    init {
        geoCaches.add (
                GeoCache ("Chair", "ITU", randomDate() )
        )
        geoCaches.add (
                GeoCache ("Bike", "Fields", randomDate() )
        )
        geoCaches.add (
                GeoCache ("Ticket", "Kobenhavns Lufthavn", randomDate() )
        )

// You can add more geocache objects if you want to
    }
    companion object : GeoCacheDBHolder<GeoCacheDB , Context>(:: GeoCacheDB )

    fun getGeoCaches () : List < GeoCache > {
        return geoCaches
    }
    fun addGeoCache(cache: String, where: String) {
        lastCache.setDate(randomDate())
        lastCache.setCache(cache)
        lastCache.setWhere(where)
        geoCaches.add(lastCache)
    }
    fun updateGeoCache ( cache : String , where : String ) {
        geoCaches.find {x -> x == lastCache}?.apply { this.setCache(cache)
                                                      this.setWhere(where)  }
    }
    fun getLastGeoCacheInfo () : String {
        return  lastCache.where
    }

    fun deleteGeoCache (cache: GeoCache){
        geoCaches.remove(cache)
    }
    private fun randomDate () : Long {
// This function gets the current timestamp and
// generates a random date in the last 365 days .
        val random = Random ()
        val now = System . currentTimeMillis ()
        val year = random . nextDouble () * 1000 * 60 * 60 * 24 * 365
        return ( now - year ) . toLong ()
    }

    open class GeoCacheDBHolder < out T : Any , in A >( creator : ( A ) -> T ) {
        private var creator : (( A ) -> T ) ? = creator
        @Volatile private var instance : T ? = null
        fun get ( arg : A ) : T {
            val checkInstance = instance
            if ( checkInstance != null )
                return checkInstance
            return synchronized ( this ) {
                val checkInstanceAgain = instance
                if ( checkInstanceAgain != null )
                    checkInstanceAgain
                else {
                    val created = creator!!(arg)
                    instance = created
                    creator = null
                    created
                }
            }
        }
    }
}