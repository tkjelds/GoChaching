package dk.itu.moapd.gocaching.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.itu.moapd.gocaching.GeoCache
import java.util.*

@Dao
interface GeoCacheDao {

    @Query("SELECT * FROM geocache")
    fun getGeoCaches(): LiveData<List<GeoCache>>

    @Query("SELECT * FROM GeoCache WHERE `where` like :where LIMIT 1")
    fun getGeoCacheByWhere(where:String): LiveData<GeoCache?>

    @Query("SELECT * FROM geocache where id=(:id)")
    fun getGeoCacheById(id:UUID): LiveData<GeoCache?>

    @Query("UPDATE GeoCache SET `where` =:where_ ,updateDate =:updateDate_ WHERE cache like :cache_ ")
    fun updateGeoByCache(where_:String, cache_:String, updateDate_:Date)
    @Insert
    suspend fun insert(geoCache: GeoCache?)

    @Update
    suspend fun update(geoCache: GeoCache?)

    @Delete
    suspend fun delete(geoCache: GeoCache?)
}