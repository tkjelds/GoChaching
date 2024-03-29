package dk.itu.moapd.gocaching.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.itu.moapd.gocaching.CachesWithUser
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.UserWithCaches
import java.util.*

@Dao
interface GeoCacheDao {

    @Query("SELECT * FROM geocache")
    fun getGeoCaches(): LiveData<List<GeoCache>>

    @Query("SELECT * FROM GeoCache WHERE `where` like :where LIMIT 1")
    fun getGeoCacheByWhere(where:String): LiveData<GeoCache?>

    @Query("SELECT * FROM geocache where gcid=(:id)")
    fun getGeoCacheById(id:UUID): LiveData<GeoCache?>

    @Query("UPDATE GeoCache SET `where` =:where_ ,updateDate =:updateDate_ WHERE cache like :cache_ ")
    fun updateGeoByCache(where_:String, cache_:String, updateDate_:Date)
    @Insert
    suspend fun insert(geoCache: GeoCache?)

    @Update
    suspend fun update(geoCache: GeoCache?)

    @Delete
    suspend fun delete(geoCache: GeoCache?)

    @Transaction
    @Query("SELECT * FROM user")
    fun getUserWithCaches(): LiveData<List<UserWithCaches>>

    @Transaction
    @Query("SELECT * FROM geocache")
    fun getCachesWithUser(): LiveData<List<CachesWithUser>>

    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<User>>

    @Insert
    suspend fun insert(user: User?)

    @Update
    suspend fun update(user: User?)

    @Delete
    suspend fun delete(user: User?)
}