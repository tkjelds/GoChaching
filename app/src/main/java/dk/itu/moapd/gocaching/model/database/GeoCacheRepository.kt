package dk.itu.moapd.gocaching.model.database

import android.app.Application
import androidx.lifecycle.LiveData
import dk.itu.moapd.gocaching.CachesWithUser
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.UserWithCaches
import java.io.File
import java.util.*

class GeoCacheRepository (application: Application) {

    private val geoCacheDao: GeoCacheDao
    private val geoCaches: LiveData< List<GeoCache>>
    private val users: LiveData<List<User>>
    private val cachesWithUser:LiveData<List<CachesWithUser>>
    private val userWithCaches:LiveData<List<UserWithCaches>>
    private val filesDir = application.filesDir

    init {
        val db = GeoCacheDatabase.get(application)
        geoCacheDao = db.geoCacheDao()
        geoCaches = geoCacheDao.getGeoCaches()
        users = geoCacheDao.getUsers()
        cachesWithUser = geoCacheDao.getCachesWithUser()
        userWithCaches = geoCacheDao.getUserWithCaches()
    }

    suspend fun insert(geoCache: GeoCache) {
        geoCacheDao.insert(geoCache)
    }

    fun getGeoCaches() = geoCaches

    suspend fun update(geoCache: GeoCache){
        geoCacheDao.update(geoCache)
    }
    suspend fun updateGeoByCache(where_:String, cache_:String, updateDate_:Date){
        geoCacheDao.updateGeoByCache(where_, cache_, updateDate_)
    }
    suspend fun delete(geoCache: GeoCache){
        geoCacheDao.delete(geoCache)
    }

    fun getGeoCacheByWhere(where:String):LiveData<GeoCache?>{
        return geoCacheDao.getGeoCacheByWhere(where)
    }

    fun getGeoCacheByWhere(id:UUID):LiveData<GeoCache?>{
        return geoCacheDao.getGeoCacheById(id)
    }


    suspend fun insert(user: User) {
        geoCacheDao.insert(user)
    }

    fun getUsers() = users

    suspend fun update(user: User){
        geoCacheDao.update(user)
    }
    suspend fun delete(user: User){
        geoCacheDao.delete(user)
    }

    fun getUserWithCaches(): LiveData<List<UserWithCaches>>
    {
        return geoCacheDao.getUserWithCaches()
    }

    fun getCachesWithUsers(): LiveData<List<CachesWithUser>>
    {
        return geoCacheDao.getCachesWithUser()
    }

    fun getPhotoFile(geoCache: GeoCache):File = File(filesDir,geoCache.photoFileName)
}